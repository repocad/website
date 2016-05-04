val core = RootProject(file("../core"))

val project = Project("website", file("."))
  .settings(
    version := "1.0",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-Xlint",
      "-deprecation"
    )
//    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core)

