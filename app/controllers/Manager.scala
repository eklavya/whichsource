package controllers

import akka.actor. { Actor, ActorRef }
import Indexing._
import akka.actor.Props

class Manager(jarPath: String) extends Actor {

  var indexing = Set.empty[ActorRef]

  override def preStart = {
    //search in path and spawn indexers, one for each jar
    val dir = new java.io.File(jarPath).listFiles().filter(_.getName().contains(".jar"))
    dir foreach { x: java.io.File =>
      context.actorOf(Props(new Indexer(x.getPath())))
    }
    context.children foreach (x => indexing += x)
  }

  def receive = beforeIndexing

  def beforeIndexing: Receive = {
    case DoneIndexing =>
      indexing = indexing - sender
      if (indexing.isEmpty) {
        context.become(afterIndexing)
      }

    case _ =>
      sender ! StillIndexing
  }

  def afterIndexing: Receive = {
    case SearchFuncs(conds) =>
      context.children.foreach(_ forward SearchFuncs(conds))
  }
}