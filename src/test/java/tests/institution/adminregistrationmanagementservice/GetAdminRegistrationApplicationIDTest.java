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
    String adminID;


    @Test()
    public void getAdminIDPositive() {
        logger.info("ARMS_17 is running");
        adminID = Helper.getAdminID();
        Response response = InstitutionEndpoints.getRegistrationApplicationsId(adminID);
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
                .body("response.institution.id", notNullValue());
    }
    @Test()
    public void getAdminInvalidId() {
        logger.info("ARMS_18 is running");
        adminID = "invalid-id";
        Response response = InstitutionEndpoints.getRegistrationApplicationsId(adminID);
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
