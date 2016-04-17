name := """pileworx-commons"""

version := "1.0.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

resolvers += "ReactiveCouchbase Snapshots" at "https://raw.github.com/ReactiveCouchbase/repository/master/snapshots/"

libraryDependencies ++= {
  val akkaV = "2.4.4"
  Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.github.nscala-time" %% "nscala-time" % "2.12.0",
    "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.4-SNAPSHOT",

    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test")
}
