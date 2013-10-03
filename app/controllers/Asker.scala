package controllers

import akka.actor.{ Cancellable, Actor, ActorRef }
import Indexing._
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import play.api.libs.concurrent.Execution.Implicits._
import play.api.templates.Html

class Asker extends Actor {

  def receive = {

    case SearchFuncs(conds) =>
    val holds = !conds.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
    if (holds) {
      val funcs = conds map { case (f, l) =>
        val func = funcMap(f).filter(x => (l.toInt >= x.start) && (l.toInt <= x.end)).toList
        (f, func map (f => views.html.source.render(f.jarName + " inside " + f.name.split('.').dropRight(1).last + ".java ", f.body.map(g => g.split('\n').toList).getOrElse(List()), f.start, l.toInt)))
      }
      sender ! GotIt(funcs.toMap)
      context.stop(self)
      } else {
        sender ! NotIt(conds.map(_._1))
        context.stop(self)
      }
    }
}
