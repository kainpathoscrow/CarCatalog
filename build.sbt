name := """car-catalog"""
organization := "com.github.kainpathoscrow"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

lazy val slickVersion = "5.0.0"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.postgresql" % "postgresql" % "42.2.11",
  "com.typesafe.play" %% "play-slick" % slickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % slickVersion,
  "com.github.tminglei" %% "slick-pg" % "0.19.0"
)