package controllers

import play.api._
import play.api.mvc._
import controllers.Indexing.{ YesIHaveIt, SearchFuncs }
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
import controllers.Indexing._
import play.api.libs.concurrent.Execution.Implicits._
import Indexing._
import akka.pattern.AskTimeoutException
import play.api.templates.Html

object Application extends Controller {
  implicit val timeout = Timeout(6.second)

  val traceForm = Form(
    tuple(
      "trace" -> text,
      "submit" -> ignored(AnyRef)))

  def index = Action {
    Ok(views.html.index("Paste your stacktrace here."))
  }

  def sourceFinder = Action { implicit request =>

    traceForm.bindFromRequest().fold (
      errors => BadRequest("Not a valid stacktrace."),
      trace  => {
        val (t, _) = trace

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

        if (askers.isEmpty) {
          BadRequest("Not a valid stacktrace.")
        } else {
          Async {
            val resList = askAndAcc(askers)
            resList map { x: List[(String, Html)] =>
              var s = Map.empty[String, List[Html]]
              x foreach { y =>
                if (!s.contains(y._1)) s += (y._1 -> List[Html]())
                s = s + (y._1 -> (s(y._1) :+ y._2))
              }
              Ok(views.html.results(s.toList))
            }
          }
        }
//        Ok(views.html.source("some jar here -> some file")(funcLines)(0)(4))
      }
    )
  }



  def askAndAcc(askers: Set[(ActorRef, SearchFuncs)]) = {
    val futures = askers.map(a => a._1 ? a._2)
    val names = Future.fold(futures)(List[(String, Html)]())((a, b) => a ++ b.asInstanceOf[List[(String, Html)]])
//    val names = Future.reduce(futures) { (a, b) =>
//      a + "\n" + b
//    }
    names
  }
}