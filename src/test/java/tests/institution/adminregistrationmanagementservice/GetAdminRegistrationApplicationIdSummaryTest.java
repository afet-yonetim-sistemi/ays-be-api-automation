package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class GetAdminRegistrationApplicationIdSummaryTest {

    Logger logger = LogManager.getLogger(this.getClass());
    String applicationID;

    @Test()
    @Story("As a user I want to get detailed information about administrator registration applications summary when I use valid ID ")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryPositive() {
        logger.info("ARMS_19 is running");
        applicationID = Helper.getApplicationID();
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(true))
                .body("httpStatus", equalTo("OK"))
                .body("response.id", notNullValue())
                .body("response.institution", notNullValue());
    }
    @Test()
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative() {
        logger.info("ARMS_20 is running");
        applicationID = "invalidID";
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header",equalTo("VALIDATION ERROR"))
                .body("subErrors[0].message", containsString("must be a valid UUID"));

    }
    @Test()
    @Story("As a user I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIdSummaryNegative2() {
        logger.info("ARMS_21 is running");
        applicationID = "0d0c71be-7473-4d98-caa8-55dec809c31c"; // invalid ID with UUID format
        Response response = InstitutionEndpoints.getRegistrationApplicationsIdSummary(applicationID);
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header",equalTo("AUTH ERROR"));

    }
}
