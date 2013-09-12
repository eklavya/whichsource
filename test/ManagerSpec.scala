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

class ManagerSpec extends TestKit(ActorSystem("ManagerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }

  "Manager" should {
    "spawn indexers for jars" in {
      val m = TestActorRef(new Manager("/home/eklavya/Downloads/jars"))
      val sel = system.actorSelection(m.path + "/*")
    }
  }
}