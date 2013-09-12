package controllers

import akka.actor.{Cancellable, Actor, ActorRef}
import Indexing._
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
  var cancellable: Cancellable = _

  def receive = {
    case StillIndexing =>
      println("This better be printed.")
      s ! "Indexing in progress, please try again later."
      cancellable.cancel

    case SearchFuncs(conds) =>
      s = sender
      funcs = conds
      m ! SearchFuncs(conds)
      cancellable = context.system.scheduler.scheduleOnce(4500.milli) {
          if (jars.isEmpty) s ! "<div class='notFound'><p>Not found for these functions:</p>" +
          funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 +
          " at line " + sp._2 + "</p>") + "</div>"

          else 			        s ! "<div class='found'>Found jars for these functions:\n" +
          funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + " at line " + sp._2 + "</p>") +
          jars.foldLeft("\n\nin jars- \n<div class='jars'>")((a, jn) => a + "<p>" + jn + "</p>") +
          "</div></div>"
        }


    case x: YesIHaveIt =>
      jars = jars :+ x.jarName.split('/').toList.last
  }
}