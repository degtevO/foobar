package com.foobar.now.configuration

import akka.http.scaladsl.model.Uri
import monix.eval.Task
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

import scala.concurrent.duration.FiniteDuration

case class DatabaseConfig(driver: String,
                          connection: String,
                          username: String,
                          password: String)

case class KarmaConfig(declineDecrease: Int)

case class HttpConfig(host: String,
                      port: Int,
                      secretKey: String,
                      tokenMaxAge: FiniteDuration,
                      uploadFilesDir: String,
                      fileUploadSizeLimit: Long)

case class AppConfig(database: DatabaseConfig, http: HttpConfig, karma: KarmaConfig)

object AppConfig {
  def apply(): Task[AppConfig] =
    Task.fromEither[ConfigReaderFailures, AppConfig](err => new Exception(err.toString))(pureconfig.loadConfig[AppConfig])
}
