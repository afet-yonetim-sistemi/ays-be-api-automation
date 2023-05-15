package com.ays.stepDefinitions;



import com.ays.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.*;


public class userStepDef {

    String requestBody;

    Response response;


    @Given("body request credentials")
    public void body_request_credentials() {
        requestBody ="{\n" +
                "    \"username\": \"232180\",\n" +
                "    \"password\": \"367894\"\n" +
                "}";
    }

    @When("I sent post request to {string} endpoint with request body")
    public void iSentPostRequestToEndpointWithRequestBody(String endpoint) {
        response = given().accept(ContentType.JSON).log().body()
                .and()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(ConfigurationReader.getProperty("APIURL") +endpoint);

        System.out.println(response.prettyPeek().body());

    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        System.out.println("response.statusCode() = " + response.statusCode());

        //Verify status code
        Assert.assertEquals(expectedStatusCode, response.statusCode());


    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {

        System.out.println("response.contentType() = " + response.contentType());

        Assert.assertEquals(expectedContentType,response.contentType());

        response.prettyPeek();

    }




}
