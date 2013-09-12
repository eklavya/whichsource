package test

import akka.actor.{Props, Actor, ActorSystem}
import akka.testkit.{TestKit, TestActorRef, ImplicitSender}
import org.scalatest.{WordSpec, BeforeAndAfterAll}
import org.scalatest.matchers.MustMatchers
import collection.JavaConversions._
import akka.pattern.ask
import akka.testkit.TestActorRef
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import controllers._
import akka.util.Timeout

class IndexerSpec extends TestKit(ActorSystem("IndexerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }

  "Indexer" should {
    import DataProvider._
    import Indexing._

    "have these functions" in {
      implicit val timeout = Timeout(5.seconds)
      val i = TestActorRef(new Indexer("/home/eklavya/Downloads/jars/hibernate-3.2.0.ga-sources.jar"))
      val f = i ? SearchFuncs(getFuncs)
      val r = f.mapTo[YesIHaveIt].value.get.get

      r.jarName must be("/home/eklavya/Downloads/jars/hibernate-3.2.0.ga-sources.jar")
    }

    "not have these functions" in {
      implicit val timeout = Timeout(5.seconds)
      val i = TestActorRef(new Indexer("/home/eklavya/Downloads/jars/hibernate-3.2.0.ga-sources.jar"))
      val f = i ? SearchFuncs(List(("System.out.println", "1")))
      val r = f.value.get.get
      r.getClass must be(NoIDont.getClass)
    }
  }
}