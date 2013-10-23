package test

import akka.actor.{ Props, Actor, ActorSystem, ActorRef }
import akka.testkit.{ TestKit, TestActorRef, ImplicitSender }
import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.MustMatchers
import collection.JavaConversions._
import akka.pattern.ask
import akka.testkit.TestActorRef
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import controllers._
import akka.util.Timeout
import DataProvider._
import models._
import Indexing._
import DataProvider._

class TestActor extends Actor {
  var msgd: Boolean = false

  def receive = {
    case DoneIndexing => msgd = true
  }
}

class TestIndexer(jarPath: String, f: java.io.File, manager: ActorRef) extends Indexer(jarPath, f, manager) {
  override def addFunc(fName: String, f: Func) {
    TestFunctions.add(fName, f)
  }
}

class IndexerSpec extends TestKit(ActorSystem("IndexerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {

  implicit val timeout = Timeout(5.seconds)
  var act: ActorRef = _
  var jarFile: java.io.File = _
  var indexer: TestIndexer = _
  var undAct: TestActor = _

  override def afterAll() {
    system.shutdown()
  }

  override def beforeAll() {
    TestFunctions.load
    act = TestActorRef[TestActor]
    undAct = act.asInstanceOf[TestActorRef[TestActor]].underlyingActor
    jarFile = new java.io.File("testJars")
    indexer = new TestIndexer("test/hibernate-3.2.0.ga-sources.jar", jarFile, act)
    indexer.index
  }

  "Indexer" should {


    "index functions" in {
      val res = TestFunctions.findFunc("org.hibernate.action.BulkOperationCleanupAction.init", 101)
      res.head.jarName must be("hibernate-3.2.0.ga-sources.jar")
      res.head.name must be("org.hibernate.action.BulkOperationCleanupAction.init")
    }
    //
    "index method invocations" in {
      val res = TestFunctions.findFunc("org.hibernate.action.BulkOperationCleanupAction.init", 101)
      res.head.body.get.contains("<a href='/func/org.hibernate.action.BulkOperationCleanupAction.evictCollectionRegions&hibernate-3.2.0.ga-sources.jar'>evictCollectionRegions</a>") must be(true)
    }

    "make a jar entry after indexing" in {
      scala.io.Source.fromFile(jarFile).getLines.contains("test/hibernate-3.2.0.ga-sources.jar") must be(true)
    }

    "inform manager when indexing is complete" in {
      undAct.msgd must be(true)
    }

  }
}