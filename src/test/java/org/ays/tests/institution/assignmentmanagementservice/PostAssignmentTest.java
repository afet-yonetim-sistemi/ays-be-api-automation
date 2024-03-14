package org.ays.tests.institution.assignmentmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;

public class PostAssignmentTest {
    Assignment assignment;

    @BeforeMethod(alwaysRun = true)
    public void setupData() {
        assignment = Assignment.generate();
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void createAnAssignmentPositive() {
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDescriptionData", dataProviderClass = DataProvider.class)
    public void createAssignmentWithInvalidDescription(String invalidDescription, String errorMessage) {
        assignment.setDescription(invalidDescription);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", anyOf(equalTo(errorMessage), equalTo("size must be between 2 and 2048")))
                .body("subErrors[0].field", equalTo("description"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCountryCodeData", dataProviderClass = DataProvider.class)
    public void createAssignmentWithInvalidCountryCode(String countryCode) {
        assignment.getPhoneNumber().setCountryCode(countryCode);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
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


    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidLineNumberData", dataProviderClass = DataProvider.class)
    public void createAssignmentWithInvalidLineNumber(String lineNumber) {
        assignment.getPhoneNumber().setLineNumber(lineNumber);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
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

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidLongitudeValues", dataProviderClass = DataProvider.class)
    @Story("As an admin user when I create an assignment with invalid longitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLongitude(Double invalidLongitude, String errorMessage) {
        assignment.setLongitude(invalidLongitude);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLongitude)));
    }


    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidLatitudeValues", dataProviderClass = DataProvider.class)
    @Story("As an admin user when I create an assignment with invalid latitude input I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLatitude(Double invalidLatitude, String errorMessage) {
        assignment.setLatitude(invalidLatitude);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("latitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo(String.valueOf(invalidLatitude)));
    }

    @Test(groups = {"Regression", "Institution"}, enabled = false)
    public void createAssignmentWithMissingLatitude() {
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 90", "must be greater than or equal to -90"))
                .body("subErrors.field", hasItems("latitude", "latitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test(groups = {"Regression", "Institution"}, enabled = false)
    public void createAssignmentWithMissingLongitude() {
        assignment.setLongitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors.message", hasItems("must be less than or equal to 180", "must be greater than or equal to -180"))
                .body("subErrors.field", hasItems("longitude", "longitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }


    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidFirstNamesAndLastDataForAssignment", dataProviderClass = DataProvider.class)
    public void createAssignmentWithInvalidFirstName(String firstName, String errorMessage) {
        assignment.setFirstName(firstName);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidFirstNamesAndLastDataForAssignment", dataProviderClass = DataProvider.class)
    @Story("As an admin when I create an assignment with invalid last name I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithInvalidLastName(String lastName, String errorMessage) {
        assignment.setLastName(lastName);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }


    @Test(groups = {"Regression", "Institution"})
    @Story("As an admin when I create an assignment with null Phone number I want to get a proper error message.")
    @Severity(SeverityLevel.NORMAL)
    public void createAssignmentWithMissingPhoneNumber() {
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumberRequest"));
    }


    @Test(groups = {"Regression", "Institution"})
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
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors.field", hasItems("latitude", "longitude", "phoneNumber", "firstName", "lastName", "description"));

    }
}
