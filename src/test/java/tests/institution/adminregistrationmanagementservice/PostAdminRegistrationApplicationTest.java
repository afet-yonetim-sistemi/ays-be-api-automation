package tests.institution.adminregistrationmanagementservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import endpoints.InstitutionEndpoints;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;
import payload.ApplicationRegistration;
import payload.Helper;
import utility.DataProvider;

import static org.hamcrest.Matchers.*;

public class PostAdminRegistrationApplicationTest extends DataProvider {
    Logger logger = LogManager.getLogger(this.getClass());
    ApplicationRegistration application = new ApplicationRegistration();


    @Test
    public void createAnAdminRegistrationApplication() {
        logInfo("ARMS_Create_01 Story:As a Super Admin I want to create an admin under an Institution", true);
        application = Helper.generateApplicationRegistrationPayload();
        logInfo(logRequestPayload(application), true);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        logInfo(logResponseBody(response), true);
        response.then()
                .spec(successResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(dataProvider = "invalidDataForPostApplicationReasonField")
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {
        logInfo("ARMS_Create_02 Story:As a Super Admin when I create an admin under an Institution with invalid reason or institution id I want to get a proper error message", true);
        application = Helper.generateApplicationRegistrationPayloadWithoutReason();
        application.setReason(reason);
        logInfo(logRequestPayload(application), true);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        logInfo(logResponseBody(response), true);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {
        logInfo("ARMS_Create_03 Story:As a Super Admin when I create an admin registration application with invalid institution ID I want to get a proper error message", true);
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId("invalidId");
        logInfo(logRequestPayload(application), true);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        logInfo(logResponseBody(response), true);
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
        logInfo("ARMS_Create_04 Story:As a Super Admin when I create an admin registration application with missing institution ID I want to get a proper error message", true);
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId(null);
        logInfo(logRequestPayload(application), true);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        logInfo(logResponseBody(response), true);
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

    private String logResponseBody(Response response) {
        if (response != null) {
            try {
                String responseBody = response.getBody().asString();
                logger.info("Response Body:\n" + responseBody);
                return responseBody;
            } catch (Exception e) {
                logger.error("Error while logging response body: " + e.getMessage());
            }
        } else {
            logger.warn("Response is null");
        }

        return null;
    }

    private String logRequestPayload(Object requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestBodyJson = mapper.writeValueAsString(requestBody);
            logger.info("Request Body: " + requestBodyJson);
            return requestBodyJson;
        } catch (JsonProcessingException e) {
            logger.error("Error converting request body to JSON: " + e.getMessage());
        }

        return null;
    }

    private void logInfo(String message, boolean printToConsole) {
        logger.info(message);
        Reporter.log(message, printToConsole);
    }

}
