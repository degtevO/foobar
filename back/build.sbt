val monixV = "3.0.0-RC2"
val `akka-httpV` = "10.1.8"
val `akka-streamsV` = "2.5.19"
val doobieV = "0.6.0"
val pureconfigV = "0.11.0"
val logbackV = "1.2.3"
val `scala-loggingV` = "3.9.2"
val circeV = "0.10.0"
val `jwt-circeV` = "2.1.0"
val akkaHttpCirceVersion = "1.25.2"

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.foobar",
      scalaVersion := "2.12.8",
      version := "0.1.0"
    )),
    name := "foobar",
    libraryDependencies ++= Seq(
      "io.monix" %% "monix" % monixV,
      "com.typesafe.akka" %% "akka-http" % `akka-httpV`,
      "com.typesafe.akka" %% "akka-stream" % `akka-streamsV`,
      "org.tpolecat" %% "doobie-core" % doobieV,
      "org.tpolecat" %% "doobie-hikari" % doobieV,
      "org.tpolecat" %% "doobie-postgres" % doobieV,
      "com.github.pureconfig" %% "pureconfig" % pureconfigV,
      "ch.qos.logback" % "logback-classic" % logbackV,
      "com.typesafe.scala-logging" %% "scala-logging" % `scala-loggingV`,
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,
      "com.pauldijou" %% "jwt-circe" % `jwt-circeV`,
      "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion
    ),
    scalacOptions += "-Ypartial-unification"
  )
