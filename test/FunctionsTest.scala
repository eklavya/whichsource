/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 8/11/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */

package test

import org.scalatest.{WordSpec, BeforeAndAfterAll}
import org.scalatest.matchers.MustMatchers
import models._
import java.io.File

object TestFunctions extends FunctionStore {
  def getPath = "test/testMap"
}

class FunctionsTest extends WordSpec with MustMatchers with BeforeAndAfterAll {

  override def beforeAll {
    TestFunctions.load
  }

  override def afterAll {
    val f = new File("test/testMap")
    if (f.exists) f.delete
  }

  "Function model" should {
    val func = new Func("TestFunction", 1, 2, Some("body"), "jar")

    "create new map if no map to load" in {
      TestFunctions.funcMap.size must be(0)
    }

    "add a new function" in {
      TestFunctions.add("TestFunction", func)
      TestFunctions.funcMap.contains("TestFunction") must be(true)
    }

    "return added functions" in {
      TestFunctions.add("TestFunction", func)
      val fl = TestFunctions.getFunc("TestFunction", "jar").get
      fl.head.name must be("TestFunction")
    }

    "store a function map" in {
      TestFunctions.add("TestFunction", func)
      TestFunctions.store
      val f = new File("test/testMap")
      f.exists must be(true)
    }

    "have new functions added in the stored map" in {
      TestFunctions.add("TestFunction", func)
      TestFunctions.store
      TestFunctions.load
      val fl = TestFunctions.getFunc("TestFunction", "jar").get
      fl.head.name must be("TestFunction")
    }

    "load a function map" in {
      TestFunctions.add("TestFunction", func)
      TestFunctions.store
      TestFunctions.load
      val fl = TestFunctions.getFunc("TestFunction", "jar").get
      fl.head.name must be("TestFunction")
    }
  }
}
