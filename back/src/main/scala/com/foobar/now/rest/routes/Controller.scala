package com.foobar.now.rest.routes

import akka.http.scaladsl.server.Route

trait Controller {
 val route: Route
}
