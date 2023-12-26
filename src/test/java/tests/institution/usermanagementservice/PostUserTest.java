package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUserTest {
    User userPayload;
    UserCredentials userCredentials;

    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod
    public void setupData() {
        userPayload = Helper.createUserPayload();
    }

    @Test()
    public void createAUser() {
        logger.info("Test case UMS_01 is running");
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
        response.then()
                .contentType("application/json")
                .body("response", notNullValue())
                .body("response.username", notNullValue())
                .body("response.password", notNullValue());
    }

    @Test(dataProvider = "countryCodeData")
    public void createUserWithInvalidCountryCode(String countryCode) {
        userPayload.getPhoneNumber().setCountryCode(countryCode);
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false));
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

    @Test(dataProvider = "lineNumberData")
    public void createUserWithInvalidLineNumber(String lineNumber) {
        userPayload.getPhoneNumber().setLineNumber(lineNumber);
        System.out.println(lineNumber);
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false));
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
                {Helper.generateInvalidLineNumber()},
                {Helper.generateLineNumber() + "*"},
                {""}, {"          "},
                {null},
                {Helper.generateLineNumber() + "a"}
        };

    }

    @Test(dataProvider = "userFirstNameLastNameData")
    public void createUserWithInvalidFirstnameAndLastname(String firstName, String lastName, String message) {
        userPayload.setFirstName(firstName);
        userPayload.setLastName(lastName);
        logger.info("Test case UMS_02-25 are running");
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .contentType("application/json")
                .statusCode(400)
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo(message));
    }

    @DataProvider(name = "userFirstNameLastNameData")
    public Object[][] userData() {
        return new Object[][]{
                {null, "Mehmet", "must not be blank"},
                {"", "Mehmet", "must not be blank"},  // UMS_02
                {"4", "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_03
                {"44", "Mehmet", "MUST BE VALID"},  // UMS_04
                {"A", "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_05
                {"A".repeat(256), "Mehmet", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_06
                {"Ahmet", "1", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_10
                {"Ahmet", "12", "MUST BE VALID"},  // UMS_11
                {"Ahmet", "M", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_12
                {"Ahmet", "M".repeat(256), "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},  // UMS_13
                {"Ahmet", "", "must not be blank"},
                {"Ahmet", null, "must not be blank"},

        };
    }


}
