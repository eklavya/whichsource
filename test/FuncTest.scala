/**
 * Created with IntelliJ IDEA.
 * User: eklavya
 * Date: 8/11/13
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import models._

class FuncTest extends WordSpec with MustMatchers {
  "Func class" should {

    val f = new Func("func", 1, 2, Some("body"), "jar")
    val fl = f.toList

    "be properly converted to list of strings" in {
      fl.size must be(5)
      fl.head must be("func")
      fl.drop(1).head must be("1")
      fl.drop(2).head must be("2")
      fl.drop(3).head must be("body")
      fl.drop(4).head must be("jar")
    }

    "be properly constructed from a list of strings" in {
      val nf = Func.fromList(fl)
      nf.name must be(f.name)
      nf.start must be(f.start)
      nf.end must be(f.end)
      nf.body must be(f.body)
      nf.jarName must be(f.jarName)
    }
  }
}
