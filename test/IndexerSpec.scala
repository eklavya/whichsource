package test

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.MustMatchers
import scala.concurrent.duration._
import akka.util.Timeout
import models._
import DataProvider._
import play.api.test._

object TestIndexer extends IndexerService {

  val jarListBackup = "test/testJars"
  val jarDir = "test/"
  var jarsToIndex = jars.length

  def addFunc(fName: String, f: Func) {
    TestFunctions.add(fName, f)
  }

  def persistIndex = ()
}

class IndexerSpec extends TestKit(ActorSystem("IndexerSpec")) with WordSpec with MustMatchers with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }

  override def beforeAll() {
    TestFunctions.load
    val testJars = new java.io.File("test/testJars")
    if (testJars.exists) testJars.delete
  }

  "Indexer" should {

    "index functions" in new WithApplication {
      TestIndexer.index("test/hibernate-3.2.0.ga-sources.jar")
      val res = TestFunctions.findFunc("org.hibernate.action.BulkOperationCleanupAction.init", 101)
      res.head.jarName must be("hibernate-3.2.0.ga-sources.jar")
      res.head.name must be("org.hibernate.action.BulkOperationCleanupAction.init")
    }

    "index method invocations" in new WithApplication {
      TestIndexer.index("test/hibernate-3.2.0.ga-sources.jar")
      val res = TestFunctions.findFunc("org.hibernate.action.BulkOperationCleanupAction.init", 101)
      res.head.body.get.contains("<a href='/func/org.hibernate.action.BulkOperationCleanupAction.evictCollectionRegions&hibernate-3.2.0.ga-sources.jar'>evictCollectionRegions</a>") must be(true)
    }

    "make a jar entry after indexing" in new WithApplication {
      TestIndexer.index("test/hibernate-3.2.0.ga-sources.jar")
      scala.io.Source.fromFile("test/testJars").getLines.contains("test/hibernate-3.2.0.ga-sources.jar") must be(true)
    }
  }
}