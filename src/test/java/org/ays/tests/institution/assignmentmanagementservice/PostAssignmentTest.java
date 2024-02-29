package org.ays.tests.institution.assignmentmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Helper;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;

public class PostAssignmentTest {
    Assignment assignment;

    @BeforeMethod
    public void setupData() {
        assignment = Helper.createAssignmentPayload();
    }

    @Test
    @Story("As an admin I want to create an assignment.")
    @Severity(SeverityLevel.NORMAL)
    public void createAnAssignmentPositive() {
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(successResponseSpec());

    }

    @Test(dataProvider = "invalidDescriptionData",dataProviderClass = DataProvider.class)
    @Story("As an admin when I create an assignment with invalid description I want to get a proper error messages.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidDescription(String invalidDescription, String errorMessage) {
        assignment.setDescription(invalidDescription);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("description"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(dataProvider = "invalidCountryCodeData",dataProviderClass = DataProvider.class)
    @Story("As an admin when I create an assignment with invalid country code input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidCountryCode(String countryCode) {
        assignment.getPhoneNumber().setCountryCode(countryCode);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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


    @Test(dataProvider = "invalidLineNumberData",dataProviderClass = DataProvider.class)
    @Story("As an admin user when I create an assignment with invalid line number I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLineNumber(String lineNumber) {
        assignment.getPhoneNumber().setLineNumber(lineNumber);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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

    @Test(dataProvider = "invalidLongitudeValues",dataProviderClass = DataProvider.class)
    @Story("As an admin user when I create an assignment with invalid longitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLongitude(Double invalidLongitude, String errorMessage) {
        assignment.setLongitude(invalidLongitude);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLongitude)));
    }


    @Test(dataProvider = "invalidLatitudeValues",dataProviderClass = DataProvider.class)
    @Story("As an admin user when I create an assignment with invalid latitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLatitude(Double invalidLatitude, String errorMessage) {
        assignment.setLatitude(invalidLatitude);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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
        assignment.setLatitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 90", "must be greater than or equal to -90"))
                .body("subErrors.field", hasItems("latitude", "latitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test()
    @Story("As an admin when I create an assignment with not a number value of longitude I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithMissingLongitude() {
        assignment.setLongitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 180", "must be greater than or equal to -180"))
                .body("subErrors.field", hasItems("longitude", "longitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }


    @Test(dataProvider = "invalidFirstNamesAndLastDataForAssignment",dataProviderClass = DataProvider.class)
    @Story("As an admin when I create an assignment with invalid first name I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidFirstName(String firstName, String errorMessage) {
        assignment.setFirstName(firstName);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(dataProvider = "invalidFirstNamesAndLastDataForAssignment",dataProviderClass = DataProvider.class)
    @Story("As an admin when I create an assignment with invalid last name I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLastName(String lastName, String errorMessage) {
        assignment.setLastName(lastName);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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
        assignment.setFirstName("a");
        assignment.setLastName("b");
        assignment.getPhoneNumber().setCountryCode("+90");
        assignment.getPhoneNumber().setLineNumber("34");
        assignment.setDescription("a");
        assignment.setLongitude(-200.0);
        assignment.setLatitude(-200.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors.field", hasItems("latitude", "longitude", "phoneNumber", "firstName", "lastName", "description"));

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
