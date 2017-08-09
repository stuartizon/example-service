package com.ovoenergy.example.resources

import com.ovoenergy.commons.lang.{DetailedErrorMessage, ServiceError}
import com.ovoenergy.commons.service.HttpService
import com.ovoenergy.example.entities.GreetingResponse
import com.ovoenergy.example.util.TraceDirectives

trait ExampleResource extends HttpService with TraceDirectives {

  val exampleResource =
    get {
      path("greet") {
        traceName("GREETING") {
          parameter('name) { name =>
            complete {
              GreetingResponse(s"Hello $name")
            }
          }
        }
      } ~
        path("leave") {
          traceName("LEAVE") {
            parameter('name) { name =>
              complete(500, DetailedErrorMessage("Oops", "Oh no", "I'm sorry"))
            }
          }
        }
    }
}