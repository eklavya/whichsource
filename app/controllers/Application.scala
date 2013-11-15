package controllers

import play.api.mvc._
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.concurrent.Akka
import akka.pattern.ask
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import models.Indexing._
import play.api.templates.Html
import java.io._
import models._

object Application extends Controller {
  implicit val timeout = Timeout(6.second)

  val traceForm = Form(
    tuple(
      "trace" -> text,
      "submit" -> ignored(AnyRef)))

  def index = Action {
    Ok(views.html.index("Paste your stacktrace here."))
  }

  def sourceFinder = Action.async { implicit request =>
    traceForm.bindFromRequest().fold(
      errors => Future(BadRequest("Not a valid stacktrace.")),
      trace => {
        val (t, _) = trace
        gatherTraceInfo(t)
      })
  }

  def gatherTraceInfo(trace: String): Future[SimpleResult] = {
    val askers = spawnAskers(trace)
    if (askers.isEmpty) {
      Future(BadRequest("Not a valid stacktrace."))
    } else {
      val resMap = askAndAcc(askers)
      resMap map { x: Map[String, List[Html]] =>
        processStack(trace, x)
      }
    }
  }

  def spawnAskers(t: String) = {
    val funcLines = t.split('\n').filter(_.contains("java:")).toList

    val funcs = funcLines map { x =>
      val s = x.split(' ').last.split('(')
      val f = s.head
      val l = s.last.split(':').last.split(')').head
      (f, l)
    }

    val fqns = funcs map (p => p._1)

    val pkgPrefixes = fqns map { x =>
      x.split('.').takeWhile(_.charAt(0).isLower).mkString(".")
    }

    val prefixSet = pkgPrefixes.toSet

    val groups = prefixSet map { x =>
      funcs.filter { case (k, v) => k.contains(x) }
    }

    val askers = groups map { g =>
      (Akka.system.actorOf(Props[Asker]), SearchFuncs(g))
    }
    askers
  }

  def askAndAcc(askers: Set[(ActorRef, SearchFuncs)]) = {
    val futures = askers.map(a => a._1 ? a._2)
    val names = Future.fold(futures)(Map.empty[String, List[Html]])((a, b) => b match {
      case GotIt(funcs) => a ++ b.asInstanceOf[GotIt].funcs
      case NotIt(fl) => a ++ fl.foldLeft(Map.empty[String, List[Html]])((a, f) => a + (f -> List(Html("<div><p class='notFound'>Not Found</p></div>")))) //map((_, )).toMap
    })
    names
  }

  def processStack(trace: String, fMap: Map[String, List[Html]]) = {
    val resl = fMap.toList
    val h = Html(trace.split('\n').map { x =>
      if (x.contains(".java:")) {
        val i = resl.zipWithIndex.find(_._1._1 == x.split(' ').last.split('(').head).get._2
        "<li><p title='" + x + "' class='traceLine' id='func" + i + "' onclick='showSource(" + i + ")'>" +
          shortHand(x) + "</p></li>"
      } else {
        x
      }
    }.mkString(" "))
    Ok(views.html.results(resl, h))
  }

  def shortHand(s: String): String = {
    val list = s.split('.').zipWithIndex
    list.map { x =>
      if (x._2 > 4 && x._2 < (list.size - 3)) "."
      else if (x._2 < (list.size - 3))        "" + x._1.head
      else                                    x._1
    }.mkString(".")
  }

  def getFunc(f: String) = Action {
    val fName = f.split('&')(0)
    val jarName = f.split('&')(1)
    Functions.getFunc(fName, jarName).map { func =>
      Ok(views.html.function(func.map{ f =>
        views.html.source.render(f.jarName + " inside " + f.name.split('.').dropRight(1).last +".java ", f.body.map(g => g.split('\n').toList).getOrElse(List()), f.start, 0)
        }.toList, Html("<p class='traceLine'>" + fName + "</p>")))
    }.getOrElse(Ok(views.html.function(List(Html("This function is not available.")), Html("<p>" + fName + "</p>"))))
  }

  def uploadJar = Action(parse.multipartFormData) { request =>
    request.body.file("jar").map { jar =>
      val filename = jar.filename
      jar.ref.moveTo(new File(s"jars/$filename"))
      MapIndexer.index(s"jars/$filename")
      Ok("Jar uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }
}