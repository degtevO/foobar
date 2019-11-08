package com.foobar.now.database

import cats.effect.Resource
import com.foobar.now.configuration.DatabaseConfig
import doobie.hikari
import monix.eval.Task
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object DatabaseTransactor {

  def apply(config: DatabaseConfig): Resource[Task, HikariTransactor[Task]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[Task](4)
      te <- ExecutionContexts.cachedThreadPool[Task]
      xa <- HikariTransactor.newHikariTransactor[Task](
        config.driver, config.connection, config.username,
        config.password, ce, te)
    } yield xa
}
