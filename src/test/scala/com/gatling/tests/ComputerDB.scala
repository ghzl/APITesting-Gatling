package com.gatling.tests

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ComputerDB extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,id;q=0.8")
		.doNotTrackHeader("1")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")


	val scn = scenario("ComputerDB")
		.exec(http("ComputerDataBasePage")
			.get("/computers"))
		.pause(3)
		.exec(http("NewComputerPage")
			.get("/computers/new"))
		.pause(21)
		.exec(http("CreateNewComputer")
			.post("/computers")
			.formParam("name", "Apple 1")
			.formParam("introduced", "2000-01-01")
			.formParam("discontinued", "2030-01-01")
			.formParam("company", "1"))
		.pause(duration = 10)
		.exec(http("FilterComputer")
			.get("/computers?f=MyComputers1")
		)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}