package controllers

import akka.actor.{Cancellable, Actor, ActorRef}
import Indexing._
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import play.api.libs.concurrent.Execution.Implicits._
import play.api.templates.Html

class Asker extends Actor {

  val m = context.actorSelection("akka://application/user/Manager")
  var s: ActorRef = _
  var jars: List[YesIHaveIt] = List()
  var funcs: List[(String, String)] = _
  var cancellable: Cancellable = _

  def receive = {
    case StillIndexing =>
      s ! List(("Indexing in progress, please try again later.", Html("")))
      cancellable.cancel
      context.stop(self)

    case SearchFuncs(conds) =>
      s = sender
      funcs = conds
      m ! SearchFuncs(conds)
      cancellable = context.system.scheduler.scheduleOnce(4500.milli) {
        if (jars.isEmpty) {
          val list = funcs.map(x => (x._1, Html("<div><p>Not Found</p></div>")))
           s ! list
           context.stop(self)
        } else {
          val list = jars.foldLeft(List[(String, Html)]()){ (a, x) =>
            a ++ x.funcs.map{ case(name, f) =>
              (name, views.html.source.render(x.jarName + " inside " + name.split('.').dropRight(1).last + ".java ", f.body.map(g=>g.split('\n').toList).getOrElse(List()), f.start, f.err))//.toInt))
            }
          }
          s ! list
          context.stop(self)
        }
        }


    case x: YesIHaveIt =>
      jars = jars :+ x
  }
}