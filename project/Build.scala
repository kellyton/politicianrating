import sbt._
import Keys._
import play.Project._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

object ApplicationBuild extends Build {

  val appName         = "politicianrating"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
   // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaJpa,
    "org.hibernate" % "hibernate-entitymanager" % "4.2.2.Final",
    "com.restfb" % "restfb" % "1.6.12",
    "mysql" % "mysql-connector-java" % "5.1.25",
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.twitter4j" % "twitter4j-stream" % "3.0.3",
    "org.twitter4j" % "twitter4j-async" % "3.0.3",
    "org.twitter4j" % "twitter4j-media-support" % "3.0.3",
    "com.google.api-client" % "google-api-client-parent" % "1.12.0-beta",
    "com.googlecode.linkedin-j" % "linkedin-j" % "1.0.416",
    "org.json"%"org.json" % "chargebee-1.0",
    "org.springframework" % "spring-context" % "3.2.3.RELEASE",
    "org.mongodb" % "mongo-java-driver" % "2.11.1",
    "org.springframework.data" % "spring-data-mongodb" % "1.2.3.RELEASE",
    "redis.clients" % "jedis" % "2.1.0",
    "org.springframework.data" % "spring-data-redis" % "1.0.5.RELEASE",
    "cglib" % "cglib" % "3.0",
    "com.google.code.gson" % "gson" % "2.2.3",
    "commons-codec" % "commons-codec" % "1.7",
    "org.apache.commons" % "commons-email" % "1.3.1",
    "javax.mail" % "mail" % "1.4.7",
    "org.apache.httpcomponents" % "httpclient" % "4.2.5",
    "org.apache.httpcomponents" % "httpmime" % "4.2.5",
    "org.apache.httpcomponents" % "httpcore-nio" % "4.2.4",
    "commons-codec" % "commons-codec" % "1.8"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    ebeanEnabled := false, 
      EclipseKeys.skipParents in ThisBuild := false      
  )

}
