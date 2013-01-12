name := "utils"

organization := "org.pinky"

version := "0.1"

scalaVersion := "2.10.0"

crossScalaVersions := Seq("2.9.2", "2.10.0")

publishMavenStyle :=true

publishArtifact in Test := false

publishTo := Some(Resolver.file("file", new File("./maven-releases")))

pomIncludeRepository := { x => false }

libraryDependencies ++= Seq( 
"log4j" % "log4j" % "1.2.16",
 "org.mockito" % "mockito-core" % "1.6" % "test",
 "com.h2database" % "h2" % "1.0.20070617" % "test",
 "org.scalatest" %% "scalatest" % "1.9.1" % "test")