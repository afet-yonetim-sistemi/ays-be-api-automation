package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class PostAssignmentTest extends InstitutionEndpoints {
    Assignment assignment;

    @BeforeMethod
    public void setupData() {
        assignment = Helper.createAssignmentPayload();
    }

    @Test
    public void createAnAssignmentPositive() {
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

    }

    @Test(dataProvider = "countryCodeData")
    public void createAssignmentWithInvalidCountryCode(String countryCode) {
        assignment.getPhoneNumber().setCountryCode(countryCode);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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
    public void createAssignmentWithInvalidLineNumber(String lineNumber) {
        assignment.getPhoneNumber().setLineNumber(lineNumber);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
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

    @Test
    public void createAssignmentWithInvalidLongitude() {
        assignment.setLongitude(2000.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be less than or equal to 180"))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo("2000.0"));
    }

    @Test
    public void createAssignmentWithInvalidLatitude() {
        assignment.setLatitude(100.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be less than or equal to 90"))
                .body("subErrors[0].field", equalTo("latitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo("100.0"));
    }

    @Test
    public void createAssignmentWithMissingFirstName() {
        assignment.setFirstName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingLastName() {
        assignment.setLastName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingDescription() {
        assignment.setDescription(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("description"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingPhoneNumber() {
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumberRequest"));
    }

    @Test
    public void createAssignmentWithMissingLatitude() {
        assignment.setLatitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must be less than or equal to 90", "must be greater than or equal to -90"))
                .body("subErrors.field", hasItems("latitude", "latitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test
    public void createAssignmentWithMissingLongitude() {
        assignment.setLongitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must be less than or equal to 180", "must be greater than or equal to -180"))
                .body("subErrors.field", hasItems("longitude", "longitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test
    public void createAssignmentWithInvalidDataCombination() {
        assignment.setFirstName(null);
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must not be null", "must not be blank"))
                .body("subErrors.field", hasItems("phoneNumber", "firstName"))
                .body("subErrors.type", hasItems("AysPhoneNumberRequest", "String"));

    }
}
