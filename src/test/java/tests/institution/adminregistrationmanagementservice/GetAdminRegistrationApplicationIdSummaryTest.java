package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class GetAdminRegistrationApplicationIdSummaryTest {

    Logger logger = LogManager.getLogger(this.getClass());
    String adminID;

    @Test()
    public void getIdSummaryPositive() {
        logger.info("ARMS_19 is running");
        adminID = Helper.getAdminID();
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(adminID);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(true))
                .body("httpStatus", equalTo("OK"))
                .body("response.id", notNullValue())
                .body("response.institution", notNullValue());
    }
    @Test()
    public void getIdSummaryNegative() {
        logger.info("ARMS_20 is running");
        adminID = "invalidID";
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(adminID);
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
    @Test()
    public void getIdSummaryNegative2() {
        logger.info("ARMS_21 is running");
        adminID = "0d0c71be-7473-4d98-caa8-55dec809c31c"; // invalid ID with UUID format
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(adminID);
        response.then()
                .log().body()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header",equalTo("AUTH ERROR"));

    }
}
