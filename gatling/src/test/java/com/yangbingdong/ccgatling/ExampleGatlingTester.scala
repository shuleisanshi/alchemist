package com.yangbingdong.ccgatling

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration.{Duration, FiniteDuration}

class ExampleGatlingTester extends Simulation {

//  private val builder = http("Example Test").get("http://127.0.0.1:8080/hello").check(bodyString.is("yangbingdong"))
  private val builder = http("Example Test").post("http://127.0.0.1:8080/auth/login/666").check(status.is(200))

  val scn: ScenarioBuilder = scenario("AuthGatlingTeste").repeat(1000) {
    exec(builder).pause(Duration.apply(5, TimeUnit.MILLISECONDS))
  }

//  val scn2: ScenarioBuilder = scenario("EpmsGatlingTeste").exec(builder)

  setUp(scn.inject(atOnceUsers(1000))).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))
  //    setUp(scn2.inject(constantUsersPerSec(50) during 10)).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))

}