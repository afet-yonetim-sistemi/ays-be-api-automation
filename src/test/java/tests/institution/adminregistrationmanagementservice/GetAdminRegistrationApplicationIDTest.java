package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payload.Helper;

import static org.hamcrest.Matchers.*;

public class GetAdminRegistrationApplicationIDTest {
    String applicationID;


    @Test()
    @Story("As a super admin I want to get detailed information about administrator registration applications when I use valid ID")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationIDPositive() {
        applicationID = Helper.getApplicationsID();
        Response response = InstitutionEndpoints.getRegistrationApplicationId(applicationID);
        response.then()
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
    @Story("As a super admin I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void getRegistrationApplicationInvalidID() {
        applicationID = "invalid-id";
        Response response = InstitutionEndpoints.getRegistrationApplicationId(applicationID);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("isSuccess", equalTo(false))
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("subErrors[0].message", containsString("must be a valid UUID"));
    }
}
