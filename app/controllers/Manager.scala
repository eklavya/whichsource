package controllers

/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 30/8/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
import akka.actor.Actor
import Indexing._
import akka.actor.Props

class Manager(jarPath: String) extends Actor {

  override def preStart = {
    //search in path and spawn indexers, one for each jar
    println(self.path)
    val dir = new java.io.File(jarPath).listFiles().filter(_.getName().contains(".jar"))
    dir foreach { x: java.io.File =>
      context.actorOf(Props(new Indexer(x.getPath())))
    }
  }

  def receive = {
    case SearchFuncs(conds) =>
      println("We got asked, forwarding!")
      context.children.foreach(_ forward SearchFuncs(conds))
  }
}