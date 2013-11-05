package controllers

import akka.actor.Actor
import models.Indexing._
import models._

class Asker extends Actor {

  def condHolds(conds: List[(String, String)]) = Functions.holds(conds)

  def findFunc(f: String, l: String) = Functions.findFunc(f, l.toInt)

  def receive = {

    case SearchFuncs(conds) =>
      val holds = condHolds(conds)//!conds.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
      if (holds) {
        val funcs = conds map { case (f, l) =>
          val func = findFunc(f, l)
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
