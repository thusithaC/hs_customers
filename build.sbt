name := """play-getting-started"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.24",
  ws
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )
