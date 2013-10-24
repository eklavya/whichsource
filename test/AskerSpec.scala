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
import models._
import akka.util.Timeout
import Indexing._
import play.api.templates.Html
import DataProvider._

class TestAsker extends Asker {
  override def condHolds(conds: List[(String, String)]) = TestFunctions.holds(conds)

  override def findFunc(f: String, l: String) = TestFunctions.findFunc(f, l.toInt)
}

class AskerSpec extends TestKit(ActorSystem("AskerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {
  implicit val timeout = Timeout(5.seconds)

  override def afterAll {
    system.shutdown
  }

  "Asker" should {

    "return result in correct format when search is successful" in {
      val i = TestActorRef[TestAsker]

      TestFunctions.load
      TestFunctions.add("test.Function.func", new Func("test.Function.func", 1, 10, Some("body"), "some jar"))

      val f = i ? SearchFuncs(List(("test.Function.func", "8")))
      //hack to get proper class name
      f.value.get.get.getClass	 must be(classOf[GotIt])
      val res = f.value.get.get.asInstanceOf[GotIt].funcs
      res.size must be(1)
      res.head._1 must be("test.Function.func")
      i.isTerminated must be(true)
    }

    "return result in correct format when search is unsuccessful due to line number mismatch" in {
      val i = TestActorRef[TestAsker]

      val f = i ? SearchFuncs(List(("test.Function.func", "11")))
      //hack to get proper class name
      f.value.get.get.getClass must be(classOf[NotIt])      
      i.isTerminated must be(true)
    }

    "return result in correct format when search is unsuccessful due to function being unavailable" in {
      val i = TestActorRef[TestAsker]

      val f = i ? SearchFuncs(List(("test.Function.func1", "9")))
      //hack to get proper class name
      f.value.get.get.getClass must be(classOf[NotIt])            
      i.isTerminated must be(true)
    }
  }
}