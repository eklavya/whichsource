package controllers

import akka.actor.Actor
import models.Indexing._
import models._
import play.api.templates.Html

class Asker extends Actor {

  def condHolds(conds: List[(String, String)]) = {
    conds map { y =>
      val samefuncs = Functions.funcMap.filter(x => x._1.contains(y._1))
      if(samefuncs.exists(x => (y._2.toInt >= x._2.last.start) && (y._2.toInt <= x._2.last.end))) {
        (y, true)
      } else {
        (y, false)
      }
    }
  }

  def findFunc(f: String, l: String) = Functions.findFunc(f, l.toInt)

  def receive = {

    case SearchFuncs(conds) =>
      val holds: List[((String, String), Boolean)] = condHolds(conds)
      val funcs = holds map { case ((f, l), e) =>
        if(e) {
          val func: Func = findFunc(f, l)
          (f + ":" + l.toString, views.html.source.render(func.jarName + " inside " + func.name.split('.').dropRight(1).last + ".java ", func.body.map(g => g.split('\n').toList).getOrElse(List()), func.start, l.toInt))
        } else {
          (f + ":" + l.toString, Html("<div><p class='notFound'>Not Found</p></div>"))
        }
      }
      sender ! GotIt(funcs)
      context.stop(self)

  }
}
