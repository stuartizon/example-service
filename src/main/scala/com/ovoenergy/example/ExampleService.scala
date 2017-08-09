package com.ovoenergy.example

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.ovoenergy.commons.lang.ConfigProvider
import com.ovoenergy.commons.monitoring.MdcLogging
import com.ovoenergy.commons.service.{DetailedServiceResponseRejectionHandler, HttpServiceActor, PingResource, TcpListener}
import com.ovoenergy.example.resources.ExampleResource
import kamon.Kamon
import spray.can.Http

object ExampleService extends App with ConfigProvider {
  Kamon.start()

  implicit val system = ActorSystem("example-service")
  val port = config.getInt("http-service.port")
  val tcpListener = system.actorOf(Props[TcpListener], "tcp-listener")
  val httpService = system.actorOf(Props[ExampleService])
  IO(Http).tell(Http.Bind(httpService, interface = "0.0.0.0", port = port), tcpListener)
}

class ExampleService extends HttpServiceActor with MdcLogging with PingResource with ExampleResource with DetailedServiceResponseRejectionHandler {
  def receive = runRoute {
    ping ~
      trace {
        logRequestResponseContext {
          handleRejections(serviceRejectionHandler) {
            handleExceptions(exceptionHandler) {
              exampleResource
            }
          }
        }
      }
  }
}