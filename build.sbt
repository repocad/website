val core = RootProject(uri("git://github.com/repocad/core"))

val project = Project("website", file("."))
  .settings(
    version := "1.0",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-Xlint",
      "-deprecation"
    )
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core)

