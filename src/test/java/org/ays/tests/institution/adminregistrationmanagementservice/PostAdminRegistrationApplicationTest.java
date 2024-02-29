package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.ApplicationRegistration;
import org.ays.payload.Helper;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

public class PostAdminRegistrationApplicationTest{
    ApplicationRegistration application;

    @BeforeMethod
    public void setup() {
        application = new ApplicationRegistration();
    }

    @Test
    public void createAnAdminRegistrationApplication() {
        application = Helper.generateApplicationRegistrationPayload();
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(successResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(dataProvider = "invalidDataForPostApplicationReasonField",dataProviderClass = DataProvider.class)
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {
        application = Helper.generateApplicationRegistrationPayloadWithoutReason();
        application.setReason(reason);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId("invalidId");
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("isSuccess", equalTo(false))
                .body("message", containsString("INSTITUTION NOT EXIST!"));
    }

    @Test
    @Story("As a Super Admin when I create an admin registration application with missing institution ID I want to get a proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void createAnAdminRegistrationApplicationWithMissingInstitutionId() {
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId(null);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("institutionId"))
                .body("subErrors[0].type", equalTo("String"));
    }


    private ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    private ResponseSpecification badRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }


}
