package com.example.iss.stepdefs;

import com.example.iss.util.Util;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralSteps {
    private Response response;
    String latitude;
    String longitude;
    Util Util = new Util();
    Faker faker = new Faker(new Locale("en-GB"));


    @Given("user call the get endpoint for satellites {string}")
    public void usercallthegetendpointforsatelite(String sateliteattribute) {

        if (sateliteattribute.matches("[0-9]+") && sateliteattribute.length() > 2) {

            response = Util.get(Util.getServiceUrlWithPath(sateliteattribute));

        } else if (sateliteattribute.isEmpty()) {

            response = Util.get(Util.getServiceUrlWithPath(""));

        } else if (sateliteattribute.contains("positions")) {

            long startTime = Instant.now().getEpochSecond();
            long endTime = Instant.now().plus(1, ChronoUnit.MINUTES).getEpochSecond();
            String POSITIONS = sateliteattribute + "/?timestamps= " + startTime + "," + endTime + "&units=miles";
            response = Util.get(Util.getServiceUrlWithPath(POSITIONS));

        } else if (sateliteattribute.contains("Incorrectpositions")) {

            long startTime = Instant.now().getEpochSecond();
            long endTime = Instant.now().minus(1, ChronoUnit.HOURS).getEpochSecond();
            String POSITIONS = sateliteattribute + "/?timestamps= " + startTime + "," + endTime + "&units=miles";
            response = Util.get(Util.getServiceUrlWithPath(POSITIONS));

        } else if (sateliteattribute.contains("tles")) {

            response = Util.get(Util.getServiceUrlWithPath(sateliteattribute));

        } else if (sateliteattribute.contains("coordinates")) {

            latitude = faker.address().latitude();
            longitude = faker.address().longitude();
            String coordinates = "/" + sateliteattribute + "/" + latitude + "," + longitude;
            response = Util.get(Util.getServiceUrlWithPath(coordinates));

        } else if (sateliteattribute.contains("coordinates/incorrect")) {

            String coordinates = "/" + sateliteattribute + "/" + RandomStringUtils.randomAlphabetic(6) + "," + RandomStringUtils.randomAlphabetic(6);
            response = Util.get(Util.getServiceUrlWithPath(coordinates));

        } else if (sateliteattribute.contains("coordinates/Incorrect/Empty")) {

            String coordinates = "/" + sateliteattribute;
            response = Util.get(Util.getServiceUrlWithPath(coordinates));
        }

    }

    @When("user receive a {int} HTTP response")
    public void userreceiveahttpresponse(Integer statuscode) {

        if (statuscode.equals(200)) {

            Assertions.assertThat(response.statusCode()).isEqualTo(statuscode);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);

        } else if (statuscode.equals(404)) {

            Assertions.assertThat(response.statusCode()).isEqualTo(statuscode);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);

        }
    }

    @Then("verify response parameters are available")
    public void verifyresponseparametersavailable(List<List<String>> outputParam) {

        JsonPath jsonPathEvaluator = response.jsonPath();
        for (int i = 0; i < outputParam.size(); i++) {

            if ((jsonPathEvaluator.getJsonObject("$").toString().contains("["))) {
                Assert.assertEquals(outputParam.get(1).get(i), ((java.util.ArrayList) jsonPathEvaluator.get((outputParam.get(0).get(i)))).get(0).toString());

            } else {
                Assert.assertEquals(outputParam.get(1).get(i), jsonPathEvaluator.get(outputParam.get(0).get(i)).toString());

            }
        }
    }

    @Then("verify response parameters for coordinates")
    public void verifyresponseparametersforcoordinates() {

        JsonPath jsonPathEvaluator = response.jsonPath();
        Assert.assertEquals(latitude, jsonPathEvaluator.get("latitude"));
        Assert.assertEquals(longitude, jsonPathEvaluator.get("longitude"));

    }

    @Then("verify error response body")
    public void verifyerrorresponsebody() {
        if (!response.asString().contains("Invalid controller specified")) {
            Assert.fail();
        }
    }

}
