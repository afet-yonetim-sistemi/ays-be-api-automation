package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.User;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUserTest {
    User userPayload;

    @BeforeMethod(alwaysRun = true)
    public void setupData() {
        userPayload = User.generate();
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void createAUser() {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response", notNullValue())
                .body("response.username", notNullValue())
                .body("response.password", notNullValue());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "countryCodeData")
    public void createUserWithInvalidCountryCode(String countryCode) {
        userPayload.getPhoneNumber().setCountryCode(countryCode);
        Response response = InstitutionEndpoints.createAUser(userPayload);
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

    @DataProvider(name = "countryCodeData")
    public Object[][] countryCodeData() {
        return new Object[][]{
                {null},
                {""},
                {"   "},
                {"090"},
                {"A90"},
                {"+90"}
        };
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "lineNumberData")
    public void createUserWithInvalidLineNumber(String lineNumber) {
        userPayload.getPhoneNumber().setLineNumber(lineNumber);
        Response response = InstitutionEndpoints.createAUser(userPayload);
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

    @DataProvider(name = "lineNumberData")
    public Object[][] lineNumberData() {
        return new Object[][]{
                {AysRandomUtil.generateInvalidLineNumber()},
                {AysRandomUtil.generateLineNumber() + "*"},
                {""}, {"          "},
                {null},
                {AysRandomUtil.generateLineNumber() + "a"}
        };

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "userFirstNameLastNameData")
    public void createUserWithInvalidFirstnameAndLastname(String firstName, String lastName, String message) {
        userPayload.setFirstName(firstName);
        userPayload.setLastName(lastName);
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(message));
    }

    @DataProvider(name = "userFirstNameLastNameData")
    public Object[][] userData() {
        return new Object[][]{
                {null, "Mehmet", "must not be blank"},
                {"", "Mehmet", "must not be blank"},
                {"4", "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"44", "Mehmet", "MUST BE VALID"},
                {"A", "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"A".repeat(256), "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Ahmet", "1", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Ahmet", "12", "MUST BE VALID"},
                {"Ahmet", "M", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Ahmet", "M".repeat(256), "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Ahmet", "", "must not be blank"},
                {"Ahmet", null, "must not be blank"},

        };
    }


}
