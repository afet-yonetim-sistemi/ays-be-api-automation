package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.Institution;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class GetAdminRegistrationApplicationIDTest {
    Logger logger = LogManager.getLogger(this.getClass());
    String institutionID;


    @Test()
    public void getInstitutionPositive() {
        logger.info("ARMS_17 is running");
        institutionID = "3cfeb994-88f4-48d0-b105-309f42f91412";
        Response response = InstitutionEndpoints.getRegistrationApplicationsId(institutionID);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(true))
                .body("httpStatus", equalTo("OK"))
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.createdUser", notNullValue())
                .body("response.status", notNullValue())
                .body("response.adminUser.firstName", notNullValue())
                .body("response.adminUser.lastName", notNullValue())
                .body("response.adminUser.email", notNullValue())
                .body("response.adminUser.id", notNullValue())
                .body("response.adminUser.phoneNumber", nullValue());
    }
    @Test()
    public void getInstitutionInvalidId() {
        logger.info("ARMS_18 is running");
        institutionID = "invalid-id";
        Response response = InstitutionEndpoints.getRegistrationApplicationsId(institutionID);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header",equalTo("VALIDATION ERROR"))
                .body("subErrors[0].message", containsString("must be a valid UUID"));
    }
}
