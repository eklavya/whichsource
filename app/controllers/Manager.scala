package controllers

import akka.actor.Actor
import java.io.FileNotFoundException
import com.typesafe.config.ConfigFactory
import models._
import Indexing._

class Manager(jarPath: String) extends Actor {

  var numJars: Int = _
  val dir = new java.io.File(jarPath).listFiles().filter(_.getName().contains(".jar"))
  val jarList = new java.io.File(ConfigFactory.load.getString("jarListBackup"))

  override def preStart = {
    Functions.load
    numJars = dir.length
    try {
      val files = io.Source.fromFile(jarList).getLines
      dir foreach { x: java.io.File =>
        if (!files.contains(x.getName)) {
//          future((new Indexer(x.getPath, jarList, self)).index)
        } else {
          numJars -= 1
        }
      }
    } catch {
      case e: FileNotFoundException =>
        dir foreach { x =>
//          future((new Indexer(x.getPath, jarList, self)).index)
        }
    }
  }

  def receive = {
    case DoneIndexing =>
      numJars -= 1
      if (numJars == 0) {
        Functions.store
        context.stop(self)
      }
  }
}
