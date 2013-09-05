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

object Application extends Controller {
  implicit val timeout = Timeout(10.second)

  val traceForm = Form(
    tuple(
      "trace" -> text,
      "submit" -> ignored(AnyRef)))

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def sourceFinder = Action { implicit request =>
    println("Got a trace")
      val (t, _) = traceForm.bindFromRequest().get
      val funcLines = t.split('\n').filter(_.contains("java:")).toList

      val funcs = funcLines map { x =>
        val s = x.split(' ').last.split('(')
        val f = s.head
        val l = s.last.split(':').last.split(')').head
        (f, l)
      }

      // val reg = """([at\\s])?(?:[a-zA-Z][a-zA-Z\\.\\d\\-]+)""".r

      // val fqdns = funcLines map { x =>
      //   (reg findAllIn x).take(2).toSeq.last
      // }

      val fqns = funcs map (p => p._1)

      val pkgPrefixes = fqns map { x =>
        x.split('.').takeWhile(_.charAt(0).isLower).mkString(".")
      }

      val prefixSet = pkgPrefixes.toSet

      val groups = prefixSet map { x =>
        funcs.filter{case(k, v) => k.contains(x)}
      }

      val askers = groups map { g =>
        (Akka.system.actorOf(Props[Asker]), SearchFuncs(g))
      }
      
      // val futures = askers.map(a => a._1 ? a._2).toSeq

//      val names = futures map {
//        x: Any =>
//          x.asInstanceOf[YesIHaveIt].jarName
//      } 
//      Ok (names.mkString(";"))
      Async {
        val res = askAndAcc(askers)
        res map {x: Any => 
          Ok(x.toString)
        }
      }

  }

  def askAndAcc(askers: Set[(ActorRef, SearchFuncs)]) = {
    val futures = askers.map(a => a._1 ? a._2).toSeq
    val names = Future.reduce(futures) { (a, b) => 
      a + ";" + b
    }
    names
  }
}