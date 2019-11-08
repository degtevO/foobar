package com.foobar.now

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import cats.effect._
import cats.syntax.all._
import com.foobar.now.configuration.AppConfig
import com.foobar.now.dao.{ChallengeDao, ChallengeTypeDao, UserDao}
import com.foobar.now.database.DatabaseTransactor
import com.foobar.now.rest.routes.{ChallengeController, ChallengeTypeController, UserController}
import com.foobar.now.rest.{HttpServer, PublicApiEndpoint}
import com.foobar.now.service.{ChallengeService, ChallengeTypeService, UserService}
import com.typesafe.scalalogging.LazyLogging
import doobie.hikari.HikariTransactor
import monix.eval._
import monix.execution.Scheduler

object WebServer extends TaskApp with LazyLogging {

  def run(args: List[String]): Task[ExitCode] = {
    (for {
      config <- Resource.liftF(AppConfig())
      xa <- DatabaseTransactor(config.database)
      _ <- initRestService(config, xa)
    } yield ())
      .use(_ => Task.never.as(ExitCode.Success))
      .onErrorHandle { ex =>
        logger.error("Error occurred during execution", ex)
        ExitCode.Error
      }
  }

  private def initRestService(config: AppConfig,
                              xa: HikariTransactor[Task]): Resource[Task, Http.ServerBinding] = {
    implicit val s: Scheduler = scheduler
    implicit val actorSystem: ActorSystem = ActorSystem("foobar")
    implicit val materializer: Materializer = ActorMaterializer()

    val userDao = new UserDao
    val challengeTypeDao = new ChallengeTypeDao()
    val challengeDao = new ChallengeDao()

    val controllers = Seq(
      new UserController(config.http, new UserService(userDao, xa)),
      new ChallengeController(config.http, new ChallengeService(config.karma, config.http, challengeTypeDao, challengeDao, userDao, xa)),
      new ChallengeTypeController(config.http, new ChallengeTypeService(new ChallengeTypeDao(), xa))
    )

    val routes = new PublicApiEndpoint(controllers).route
    Resource.liftF(HttpServer(config.http, routes))
  }

}

