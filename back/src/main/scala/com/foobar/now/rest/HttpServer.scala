package com.foobar.now.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.foobar.now.configuration.HttpConfig
import monix.eval.Task

object HttpServer {
  def apply(config: HttpConfig, routes: Route)
           (implicit materializer: Materializer,
            actorSystem: ActorSystem): Task[Http.ServerBinding] = {
    Task.deferFuture(
      Http().bindAndHandle(routes, config.host, config.port)
    )
  }
}
