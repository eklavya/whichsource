package controllers

import akka.actor.Actor
import Indexing._
import akka.actor.ActorRef
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._

class Asker extends Actor {
  val m = context.actorFor("akka://application/user/Manager")
  var s: ActorRef = _
  var jars: List[String] = List()
  var funcs: List[(String, String)] = _

  def receive = {
    case SearchFuncs(conds) =>
      funcs = conds
      m ! SearchFuncs(conds)
      s = sender
      context.system.scheduler.scheduleOnce(4500.milli) {
        if (jars.isEmpty) s ! "<div class='notFound'><p>Not found for these functions:</p>" + 
                              funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + 
                              " at line " + sp._2 + "</p>") + "</div>"

        else 			        s ! "<div class='found'>Found jars for these functions:\n" +
                              funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + " at line " + sp._2 + "</p>") + 
                              jars.foldLeft("\n\nin jars- \n<div class='jars'>")((a, jn) => a + "\n" + jn) +
                               "</div></div>"
      }

    case x: YesIHaveIt =>
      jars = jars :+ x.jarName.split('/').toList.last
  }
}