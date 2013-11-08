package test

import org.scalatest._
import org.scalatest.matchers._

import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends WordSpec with MustMatchers {

  "Application" should {
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must be(None)
    }

    "validate trace" in new WithApplication {
      val res = controllers.Application.sourceFinder()(FakeRequest(POST, "/trace"))
      status(res) must equal(400)
      contentAsString(res) must include("Not a valid stacktrace.")
    }

  }
}
