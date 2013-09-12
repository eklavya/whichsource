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

class AskerSpec extends TestKit(ActorSystem("AskerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {

  override def afterAll {
    system.shutdown
  }

  "Asker" should {
    import DataProvider._
    import Indexing._

    "ask" in {
      implicit val timeout = Timeout(5.seconds)
      val i = TestActorRef[Asker]
      val ind = i.underlyingActor
      ind.receive(YesIHaveIt("this should get added to the list."))
      ind.jars.contains("this should get added to the list.") must be(true)
    }
  }
}