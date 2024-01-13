package tests.institution.assignmentmanagementservice;

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
import payload.Assignment;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class PostAssignmentTest extends utility.DataProvider {
    Assignment assignment;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod
    public void setupData() {
        assignment = Helper.createAssignmentPayload();
    }

    @Test
    @Story("As an admin I want to create an assignment.")
    @Severity(SeverityLevel.NORMAL)
    public void createAnAssignmentPositive() {
        logger.info("Test case IAMS_11 is running");
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(successResponseSpec());

    }

    @Test(dataProvider = "invalidDescriptionData")
    @Story("As an admin when I create an assignment with invalid description I want to get a proper error messages.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidDescription(String invalidDescription, String errorMessage) {
        logger.info("Test case IAMS_12 is running");
        assignment.setDescription(invalidDescription);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("description"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(dataProvider = "invalidCountryCodeData")
    @Story("As an admin when I create an assignment with invalid country code input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidCountryCode(String countryCode) {
        logger.info("Test case IAMS_13 is running");
        assignment.getPhoneNumber().setCountryCode(countryCode);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec());
        if (countryCode == null || countryCode.equals("   ") || countryCode.equals("")) {
            response.then()
                    .body("subErrors[0].message", equalTo("must not be blank"))
                    .body("subErrors[0].field", equalTo("countryCode"))
                    .body("subErrors[0].type", equalTo("String"))
                    .body("subErrors[0].value", equalTo(countryCode));
        } else {
            response.then()
                    .body("subErrors[0].message", equalTo("MUST BE VALID"))
                    .body("subErrors[0].field", equalTo("phoneNumber"))
                    .body("subErrors[0].type", equalTo("AysPhoneNumberRequest"))
                    .body("subErrors[0].value", notNullValue());
        }
    }


    @Test(dataProvider = "invalidLineNumberData")
    @Story("As an admin user when I create an assignment with invalid line number I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLineNumber(String lineNumber) {
        logger.info("Test case IAMS_14 is running");
        assignment.getPhoneNumber().setLineNumber(lineNumber);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec());
        if (lineNumber == null || lineNumber.equals("          ") || lineNumber.equals("")) {
            response.then()
                    .body("subErrors[0].message", equalTo("must not be blank"))
                    .body("subErrors[0].field", equalTo("lineNumber"))
                    .body("subErrors[0].type", equalTo("String"))
                    .body("subErrors[0].value", equalTo(lineNumber));
        } else {
            response.then()
                    .body("subErrors[0].message", equalTo("MUST BE VALID"))
                    .body("subErrors[0].field", equalTo("phoneNumber"))
                    .body("subErrors[0].type", equalTo("AysPhoneNumberRequest"))
                    .body("subErrors[0].value", notNullValue());
        }
    }

    @Test(dataProvider = "invalidLongitudeValues")
    @Story("As an admin user when I create an assignment with invalid longitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLongitude(Double invalidLongitude, String errorMessage) {
        logger.info("Test case IAMS_15 is running");
        assignment.setLongitude(invalidLongitude);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLongitude)));
    }


    @Test(dataProvider = "invalidLatitudeValues")
    @Story("As an admin user when I create an assignment with invalid latitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLatitude(Double invalidLatitude, String errorMessage) {
        logger.info("Test case IAMS_16 is running");
        assignment.setLatitude(invalidLatitude);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("latitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLatitude)));
    }

    @Test
    @Story("As an admin when I create an assignment with not a number value of latitude I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithMissingLatitude() {
        logger.info("Test case IAMS_17 is running");
        assignment.setLatitude(Double.NaN);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 90", "must be greater than or equal to -90"))
                .body("subErrors.field", hasItems("latitude", "latitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test
    @Story("As an admin when I create an assignment with not a number value of longitude I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithMissingLongitude() {
        logger.info("Test case IAMS_18 is running");
        assignment.setLongitude(Double.NaN);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 180", "must be greater than or equal to -180"))
                .body("subErrors.field", hasItems("longitude", "longitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }


    @Test(dataProvider = "invalidFirstNamesAndLastDataForAssignment")
    @Story("As an admin when I create an assignment with invalid first name I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidFirstName(String firstName, String errorMessage) {
        logger.info("Test case IAMS_19 is running");
        assignment.setFirstName(firstName);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(dataProvider = "invalidFirstNamesAndLastDataForAssignment")
    @Story("As an admin when I create an assignment with invalid last name I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLastName(String lastName, String errorMessage) {
        logger.info("Test case IAMS_20 is running");
        assignment.setLastName(lastName);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }


    @Test
    @Story("As an admin when I create an assignment with null Phone number I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithMissingPhoneNumber() {
        logger.info("Test case IAMS_21 is running");
        assignment.setPhoneNumber(null);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumberRequest"));
    }


    @Test
    @Story("As an admin when I create an assignment with multiple invalid inputs I want to get proper error messages.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidDataCombination() {
        logger.info("Test case IAMS_22 is running");
        assignment.setFirstName("a");
        assignment.setLastName("b");
        assignment.getPhoneNumber().setCountryCode("+90");
        assignment.getPhoneNumber().setLineNumber("34");
        assignment.setDescription("a");
        assignment.setLongitude(-200.0);
        assignment.setLatitude(-200.0);
        logRequestPayload(assignment);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        logResponseBody(response);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.field", hasItems("latitude", "longitude", "phoneNumber", "firstName", "lastName", "description"));

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
