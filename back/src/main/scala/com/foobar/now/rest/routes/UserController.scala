package com.foobar.now.rest.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.foobar.now.model.{SignIn, TokenClaim}
import com.foobar.now.service.UserService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import com.foobar.now.configuration.HttpConfig
import monix.execution.Scheduler

import scala.util.{Failure, Success}

class UserController(conf: HttpConfig, userService: UserService)(implicit s: Scheduler)
  extends Controller with FailFastCirceSupport with JwtSupport with MonixSupport {

  override val route: Route = pathPrefix("user") {
    (post & path("signin")) {
      entity(as[SignIn]) { si =>
        val tokenF = userService.signIn(si).map(user => encodeToken(TokenClaim(user.id).asJson.noSpaces, conf)).runToFuture
        onComplete(tokenF){
          case Success(token) =>
            respondWithHeader(RawHeader("Access-Token", token)) {
              complete(StatusCodes.OK)
            }
          case Failure(ex) => complete(StatusCodes.Unauthorized -> ex.getMessage)
        }

      }
    } ~ withJwt(conf.secretKey) { token =>
      (get & path(LongNumber)) { userId =>
        complete(userService.get(userId))
      } ~
      (get & path("info")) {
        complete(userService.get(token.userId))
      } ~
      (get & pathEndOrSingleSlash & parameter('limit.as[Int].?)) { limit =>
        complete(userService.list(token.userId, limit))
      } ~
      (get & path("near") & parameter('limit.as[Int].?)) { limit =>
        complete(userService.near(token.userId, limit))
      } ~
      (get & path("friends") & parameter('limit.as[Int].?)) { limit =>
        complete(userService.friends(token.userId, limit))
      }
    }
  }
}