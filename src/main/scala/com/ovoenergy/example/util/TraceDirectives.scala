package com.ovoenergy.example.util

import kamon.Kamon
import kamon.trace.Tracer
import spray.routing.Directive0
import spray.routing.directives.{ExecutionDirectives, MiscDirectives, BasicDirectives}

trait TraceDirectives extends BasicDirectives with MiscDirectives with ExecutionDirectives {
  def traceName(name: String): Directive0 = mapRequest { req =>
    Tracer.currentContext.addTag("endpoint", name)
    req
  }

  def trace: Directive0 =
    requestInstance.flatMap {
      request =>
        val traceToken = request.headers.find(_.name == "X-Trace-Token").map(_.value)
        val context = Kamon.tracer.newContext("http-request", traceToken)
        Tracer.setCurrentContext(context)
        mapHttpResponse { response =>
          Kamon.metrics.counter("http-request",
            Map(
              "method" -> request.method.name,
              "host" -> request.uri.authority.host.address,
              "status" -> response.status.intValue.toString) ++ Tracer.currentContext.tags).increment()
          context.finish()
          response
        }
    }
}

object TraceDirectives extends TraceDirectives