package com.ovoenergy.example.resources

import com.ovoenergy.commons.service.HttpService
import com.ovoenergy.example.entities.GreetingResponse

trait ExampleResource extends HttpService {

  val exampleResource =
    get {
      path("greet") {
        parameter('name) {name =>
          complete {
            GreetingResponse(s"Hello $name")
          }
        }
      }
    }
}