package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class CRUDTest extends Simulation
{
  //protocol
  val httpProtocol = http
    .baseUrl("https://reqres.in/api")

  //scenario
  val scnPost = scenario("Create User")
    .exec(
      http("create user request")
        .post("/users")
        .header("content-type","application/json")
        .body(RawFileBody("test/data/user.json")).asJson
//        .body(StringBody(
//          """
//            |{
//            |    "name": "morpheus",
//            |    "job": "leader"
//            |}
//            |""".stripMargin))
        .check(
          status is 201,
          jsonPath("$.name") is "Bambang"
        )
    )
    .pause(2)

  val scnGet = scenario("Get Request")
    .exec(
      http("get user request")
        .get("/users/2")
        .header("content-type","application/json")
        .check(
          status is 200,
          jsonPath("$.data.first_name") is "Janet"
        )
    )
    .pause(5)

  val scnPut = scenario("Update User")
    .exec(
      http("update user")
        .put("/users/2")
        .header("content-type", "application/json")
        .body(StringBody(
          """
            |{
            |    "name": "morpheus",
            |    "job": "jonggol resident"
            |}
            |""".stripMargin)).asJson
        .check(
          status is 200,
          jsonPath("$.job") is "jonggol resident"
        )
    )
    .pause(2)

  val scnDelete = scenario("Delete user")
    .exec(
      http("delete user")
        .delete("/users/2")
        .check(
          status is 204
        )
    )

  //setup
  setUp(
    scnPost.inject(rampUsers(5).during(10)),
    scnGet.inject(rampUsers(2).during(4)),
    scnPut.inject(rampUsers(4).during(2)),
    scnDelete.inject(rampUsers(1).during(10))
  ).protocols(httpProtocol)

}
