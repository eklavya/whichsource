package controllers

import play.api._
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
import Indexing._
import akka.pattern.AskTimeoutException
import play.api.templates.Html
import scala.util.Marshal

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

        val askers = spawnAskers(t)

        if (askers.isEmpty) {
          Future(BadRequest("Not a valid stacktrace."))
        } else {
//           {
            val resMap = askAndAcc(askers)
            resMap map { x: Map[String, List[Html]] =>
//              var fMap = Map.empty[String, List[Html]]
//              x foreach { y =>
//                if (!fMap.contains(y._1)) fMap += (y._1 -> List[Html]())
//                fMap = fMap + (y._1 -> (fMap(y._1) :+ y._2))
//              }
              processStack(t, x)
            }
//          }
        }
      })
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
      case NotIt(fl) => a ++ fl.foldLeft(Map.empty[String, List[Html]])((a, f) => a + (f -> List(Html("<div><p class='notFound'>Not Found</p></div>"))))//map((_, )).toMap
    })
    names
  }

  def processStack(trace: String, fMap: Map[String, List[Html]]) = {
//    if (fMap.keySet.head.contains("Indexing in progress, please try again later.")) {
//      Ok(views.html.results(fMap.toList, Html(fMap.keySet.head)))
//    } else {
      val resl = fMap.toList
      val h = Html(trace.split('\n').map { x =>
        if (x.contains(".java:")) {
          val i = resl.zipWithIndex.find(_._1._1 == x.split(' ').last.split('(').head).get._2
          "<li><p class='traceLine' id='func" + i + "' onclick='showSource(" + i + ")'>" +
            x + "</p></li>"
        } else {
          x
        }
      }.mkString(" "))
      Ok(views.html.results(resl, h))
//    }
  }

}