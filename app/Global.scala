/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 30/8/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */

import akka.actor.Props
import play.api._
import controllers._
import play.api.Play.current

import play.api.libs.concurrent.Akka

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Akka.system.actorOf(Props(new Manager("/home/eklavya/Downloads/jars")), "Manager")
  }
}
