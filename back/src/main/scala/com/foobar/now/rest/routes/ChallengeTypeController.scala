package com.foobar.now.rest.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.foobar.now.configuration.HttpConfig
import com.foobar.now.model.ChallengeType
import com.foobar.now.service.ChallengeTypeService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

class ChallengeTypeController(config: HttpConfig, challengeTypeService: ChallengeTypeService)
  extends Controller with FailFastCirceSupport with JwtSupport with MonixSupport {

  override val route: Route = withJwt(config.secretKey) { token =>
    pathPrefix("challengeTypes") {
      (get & pathEndOrSingleSlash & parameter('limit.as[Int].?) & parameter('offset.as[Int].?)) { case (limit, offset) =>
        complete(challengeTypeService.list(limit, offset))
      } ~
      (post & pathEndOrSingleSlash) {
        entity(as[ChallengeType]) { challengeType =>
          complete(challengeTypeService.create(challengeType))
        }
      } ~
      (path(IntNumber) & get) { challengeTypeId =>
        complete(challengeTypeService.get(challengeTypeId))
      }
    }
  }
}
