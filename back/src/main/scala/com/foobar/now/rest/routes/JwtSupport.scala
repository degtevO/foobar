package com.foobar.now.rest.routes

import java.time.Instant

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.generic.auto._
import com.foobar.now.configuration.HttpConfig
import com.foobar.now.model.Token

trait JwtSupport {

  def withJwt(secretKey: String): Directive1[Token] = {
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwt) =>
        val maybeToken = for {
          json <- readJwt(jwt, secretKey)
          token <- json.as[Token]
        } yield token

        maybeToken.fold(_ => error400, provideIfNotExpired)
      case None => error400
    }
  }

  def encodeToken(content: String, conf: HttpConfig): String = {
    val maxAgeMillis = conf.tokenMaxAge.toMillis
    val claim = JwtClaim(
      content = content,
      expiration = Some(Instant.now.plusMillis(maxAgeMillis).toEpochMilli)
    )
    JwtCirce.encode(claim, conf.secretKey, JwtAlgorithm.HS256)
  }

  private def readJwt(jwt: String, secretKey: String) =
    JwtCirce.decodeJson(jwt, secretKey, Seq(JwtAlgorithm.HS256)).toEither

  private def provideIfNotExpired(token: Token): Directive1[Token] =
    if (token.exp < System.currentTimeMillis) error401
    else provide(token)

  private def error400 = complete(StatusCodes.Unauthorized -> "No valid token provided")
  private def error401 = complete(StatusCodes.Unauthorized -> "Token expired")
}
