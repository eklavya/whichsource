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

class Asker extends Actor {
  val m = context.actorFor("akka://application/user/Manager")
  var s: ActorRef = _
  
  override def preStart {
    println("Spawned asker")
  }

  def receive = {
    case SearchFuncs(conds) =>
      println("We got asked")
      m ! SearchFuncs(conds)
      s = sender

    case x: YesIHaveIt =>
      s ! x
  }
}