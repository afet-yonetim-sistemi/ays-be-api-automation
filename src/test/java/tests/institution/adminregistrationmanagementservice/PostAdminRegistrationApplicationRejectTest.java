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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.RejectReason;
import utility.DataProvider;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;

public class PostAdminRegistrationApplicationRejectTest extends DataProvider {
    String applicationID;
    Logger logger = LogManager.getLogger(this.getClass());
    RejectReason reason;

    @BeforeMethod
    void setup() {
        applicationID = Helper.getAdminRegistrationApplicationID();
        reason=new RejectReason();
    }
    @Test
    @Story("As a super admin when I approve an application with WAITING status I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectANotCompletedApplication() {
        logger.info("Test case ARMS_Reject_02 is running");
        reason.setRejectReason("Example reject reason above forty characters.");
        logRequestPayload(reason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID,reason);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("isSuccess", equalTo(false))
                .body("message", anyOf(
                        containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"),
                        containsString("WAITING")));
    }
    @Test(dataProvider = "invalidRejectReason")
    @Story("As a super admin when I approve an application with reason field less than 40 or more than 512 characters I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason) {
        logger.info("Test case ARMS_Reject_03 is running");
        reason.setRejectReason(invalidRejectReason);
        logRequestPayload(reason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID,reason);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("size must be between 40 and 512"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo(invalidRejectReason));
    }
    @Test()
    @Story("As a super admin when I reject an application with invalidID I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithInvalidApplicationId() {
        logger.info("Test case ARMS_Reject_04 is running");
        applicationID="invalidApplicationID";
        reason.setRejectReason("Example reject reason above forty characters.");
        logRequestPayload(reason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID,reason);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }
    @Test()
    @Story("As a super admin when I reject an application with null/missing reason field I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void rejectAnApplicationWithMissingReasonField() {
        logger.info("Test case ARMS_Reject_05 is running");
        logRequestPayload(reason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationID,reason);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"));
    }
    private void logResponseBody(Response response) {
        if (response != null) {
            try {
                String responseBody = response.getBody().asString();
                logger.info("Response Body:\n" + responseBody);
            } catch (Exception e) {
                logger.error("Error while logging response body: " + e.getMessage());
            }
        } else {
            logger.warn("Response is null");
        }
    }
    private void logRequestPayload(Object requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestBodyJson = mapper.writeValueAsString(requestBody);
            logger.info("Request Body: " + requestBodyJson);
        } catch (JsonProcessingException e) {
            logger.error("Error converting request body to JSON: " + e.getMessage());
        }
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
