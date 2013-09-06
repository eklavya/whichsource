package controllers

/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 30/8/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
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

  override def preStart {
    println("Spawned asker")
  }

  def receive = {
    case SearchFuncs(conds) =>
      println("We got asked, telling manager.")
      funcs = conds
      m ! SearchFuncs(conds)
      s = sender
      context.system.scheduler.scheduleOnce(4500.milli) {
        if (jars.isEmpty) s ! "<div class='notFound'><p>Not found for these functions:</p>" + funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + " at line " + sp._2 + "</p>") + "</div>"//mkString("function ", "at", "\n")
        else 			  s ! "<div class='found'>Found jars for these functions:\n" + funcs.foldLeft("")((a, sp) => a + "<p>" + sp._1 + " at line " + sp._2 + "</p>") + jars.foldLeft("\n\nin jars- \n<div class='jars'>")((a, jn) => a + "\n" + jn) + "</div></div>"//mkString("\n")
      }

    case x: YesIHaveIt =>
      println(self.path + " positive " + x.jarName)
      jars = jars :+ x.jarName.split('/').toList.last
  }
}