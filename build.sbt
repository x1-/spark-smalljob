name := "spark-smalljob"

version := "1.0.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-target:jvm-1.7")

crossPaths := false

testOptions += Tests.Argument("console", "junitxml")

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "com.databricks"       %% "spark-csv"      % "1.2.0",
  "com.typesafe"          % "config"         % "1.2.1",
  "org.apache.hadoop"     % "hadoop-client"  % "2.6.0-cdh5.4.7" % "provided" excludeAll ExclusionRule(organization = "javax.servlet"),
  "org.apache.spark"     %% "spark-core"     % "1.5.1" % "provided" excludeAll ExclusionRule(organization = "org.apache.hadoop"),
  "org.apache.spark"     %% "spark-sql"      % "1.5.1" % "provided" excludeAll ExclusionRule(organization = "org.apache.hadoop"),
  "org.apache.spark"     %% "spark-catalyst" % "1.5.1" % "provided" excludeAll ExclusionRule(organization = "org.apache.hadoop")
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-junit" % "3.6.1-scalaz-7.0.6" % "test",
  "org.specs2" %% "specs2-mock"  % "3.6.1-scalaz-7.0.6" % "test"
)

resolvers ++= Seq(
  "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)
