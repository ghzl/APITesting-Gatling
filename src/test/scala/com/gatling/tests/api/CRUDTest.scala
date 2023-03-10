package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class CRUDTest extends Simulation {

  //protocols
  val httpProtocol = http
    .baseUrl("https://reqres.in/api")

  //scenarios
  val createUserScn = scenario("Create User")
    .exec(http("create user req")
      .post("/users")
      .header("content-type", "application/json")
      .asJson
      .body(RawFileBody("test/data/user.json")).asJson
//      .body(StringBody(
//        """
//          |{
//          |    "name": "Bambang",
//          |    "job": "leader"
//          |}
//          |""".stripMargin)).asJson
      .check(
        status.is(201),
        jsonPath("$.name").is("Bambang")
      )
    )
    .pause(10)

  val updateUserScn = scenario("Update User")
    .exec(http("update user req")
      .put("/update/users/2")
      .body(RawFileBody("test/data/user.json")).asJson
      .check(
        status.is(200),
        jsonPath("$.name").is("Bambang")
      )
    )
    .pause(5)

  val getUserScn = scenario("Get User")
    .exec(http("get user req")
      .get("/users/2")
      .check(
        status.is(200),
        jsonPath("$.data.first_name").is("Janet")
      )
    )

  //setup
  setUp(
    createUserScn.inject(rampUsers(10).during(10)),
    updateUserScn.inject(rampUsers(5).during(5)),
    getUserScn.inject(rampUsers(5).during(5))
    ).protocols(httpProtocol)

}
