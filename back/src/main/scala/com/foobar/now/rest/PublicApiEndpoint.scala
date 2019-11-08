package com.foobar.now.rest

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.foobar.now.rest.routes.Controller

import scala.util.control.NonFatal

class PublicApiEndpoint(controllers: Seq[Controller]) {
  implicit val exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case NonFatal(e) => complete(
        HttpResponse(StatusCodes.InternalServerError, entity = s"$e - ${e.getStackTrace.mkString(",")}")
      )
    }

  val route: Route = Route.seal(pathPrefix("api" / "v1") {
    controllers.map(_.route).reduce(_ ~ _)
  } ~ path("ok") { complete("I am ok") })
}