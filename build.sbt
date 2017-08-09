import com.ovoenergy.sbt.BasicSettings

BasicSettings.default
Revolver.settings

name := "example-service"
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.ovoenergy" %% "commons-client" % "1.2.+",
  "com.ovoenergy" %% "commons-service" % "1.2.+",
  "com.ovoenergy" %% "commons-monitoring" % "1.2.+",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.kamon" %% "kamon-influxdb" % "0.6.3"
)