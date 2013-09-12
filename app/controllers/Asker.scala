package controllers

import akka.actor.Actor
import Indexing._
import akka.actor.ActorRef
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import play.api.libs.concurrent.Execution.Implicits._

class Asker extends Actor {

  override def preStart {
    println("asker spawned")
  }

  val m = context.actorSelection("akka://application/user/Manager")
  var s: ActorRef = _
  var jars: List[String] = List()
  var funcs: List[(String, String)] = _
  var setupComplete = false

  def receive = {
    case DoneIndexing =>
      println("This better be printed.")
      setupComplete = true

    case SearchFuncs(conds) =>
      s = sender
      funcs = conds
      m ! SearchFuncs(conds)
      context.system.scheduler.scheduleOnce(4500.milli) {
        if (setupComplete) {
          if (jars.isEmpty) s ! "<div class='notFound'><p>Not found for these functions:</p>" +
            funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 +
              " at line " + sp._2 + "</p>") + "</div>"

          else 			        s ! "<div class='found'>Found jars for these functions:\n" +
            funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + " at line " + sp._2 + "</p>") +
            jars.foldLeft("\n\nin jars- \n<div class='jars'>")((a, jn) => a + "<p>" + jn + "</p>") +
            "</div></div>"
        } else {
          s ! "Indexing in progress, please try again later."
        }
      }

    case x: YesIHaveIt =>
      jars = jars :+ x.jarName.split('/').toList.last
  }
}