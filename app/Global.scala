/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 30/8/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */

import models.{MapIndexer, IndexerService}
import play.api._


object Global extends GlobalSettings {

  override def onStart(app: Application) {
//    val repoXML = xml.XML.load("https://gerrit-api.commondatastorage.googleapis.com/")
//    val keys    = repoXML \ "Contents" flatMap (x => x \ "Key")
//    val jars    = keys.foldLeft(Set.empty[String])((s, x) => x match {case <Key>{l}</Key> => s + l.toString}).filter(_.endsWith("sources.jar"))
//    val ps = jars map { x: String =>
//      new URL("https://gerrit-api.commondatastorage.googleapis.com/" + x) #> new File("jars/" + x.split('/').toList.last)
//    }
//    ps.foreach(_.!)
//    Akka.system.actorOf(Props(new Manager(ConfigFactory.load.getString("repoPath"))), "Manager")
    MapIndexer.init
  }
}
