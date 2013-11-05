package test

import akka.actor.{ Actor, ActorSystem, ActorRef }
import akka.testkit.TestKit
import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.MustMatchers
import akka.testkit.TestActorRef
import scala.concurrent.duration._
import org.specs2.mock._
import akka.util.Timeout
import models.IndexerService
import models._
import Indexing._
import DataProvider._

class TestActor extends Actor {
  var msgd: Boolean = false

  def receive = {
    case DoneIndexing => msgd = true
  }
}

object TestIndexer extends IndexerService {
  def addFunc(fName: String, f: Func) {
    TestFunctions.add(fName, f)
  }

  def persistIndex = ()

  override private val indexer =


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
    indexer.index
  }

  "Indexer" should {

    jarFile = new java.io.File("test/testJars")
    indexer = new TestIndexer("test/hibernate-3.2.0.ga-sources.jar", jarFile, act)

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