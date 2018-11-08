name := "SlickTestSbt"

version := "0.1"

scalaVersion := "2.11.10"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.1.0"
)

libraryDependencies += "com.typesafe.slick" %% "slick-extensions" % "2.1.0"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.9"

libraryDependencies += "com.ibm.db2.jcc" % "db2jcc4" % "10.1"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

resolvers += "DB2 Driver" at "https://mvnrepository.com/artifact/com.ibm.db2.jcc/db2jcc4"