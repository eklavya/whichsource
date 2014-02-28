/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 30/8/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.File
import models.MapIndexer
import play.api._
import scala.io.Source


object Global extends GlobalSettings {

  override def onStart(app: Application) {
//    val jarRepo = ConfigFactory.load.getString("jarRepo")
//    val repoXML = xml.XML.load(jarRepo)
//    val keys    = repoXML \ "Contents" flatMap (x => x \ "Key")
//    val jars    = keys.foldLeft(Set.empty[String])((s, x) => x match {case <Key>{l}</Key> => s + l.toString}).filter(_.endsWith("sources.jar"))
//    jars foreach { x: String =>
//      val f = new File("jars/" + x.split('/').toList.last)
//      if (!f.exists) {
//        val s = Source.fromURL(new URL(jarRepo + x))(scala.io.Codec.ISO8859)
//        val out = new BufferedOutputStream(new FileOutputStream(f))
//        val fb = s.buffered map {
//          _.toByte
//        }
//        out.write(fb.toArray)
//        out.close
//      }
//    }
    MapIndexer.init
  }
}
