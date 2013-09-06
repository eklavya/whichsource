package controllers

import akka.actor.Actor
import Indexing._
import akka.actor.Props

class Manager(jarPath: String) extends Actor {

  override def preStart = {
    //search in path and spawn indexers, one for each jar
    val dir = new java.io.File(jarPath).listFiles().filter(_.getName().contains(".jar"))
    dir foreach { x: java.io.File =>
      context.actorOf(Props(new Indexer(x.getPath())))
    }
  }

  def receive = {
    case SearchFuncs(conds) =>
      context.children.foreach(_ forward SearchFuncs(conds))
  }
}