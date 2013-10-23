package test

import org.scalatest._
import org.scalatest.matchers._

import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Await

class ApplicationSpec extends WordSpec with MustMatchers {

  "Application" should {
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must be(None)
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equal(200)
      contentType(home).get must equal("text/html")
      contentAsString(home) must include("Paste your stacktrace here.")
    }

    "validate trace" in new WithApplication {
      val res = controllers.Application.sourceFinder()(FakeRequest(POST, "/trace"))
      status(res) must equal(400)
      contentAsString(res) must include("Not a valid stacktrace.")
    }

  }
}
