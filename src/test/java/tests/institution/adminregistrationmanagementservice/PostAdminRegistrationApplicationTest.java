package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import payload.ApplicationRegistration;
import payload.Helper;
import utility.DataProvider;

import static org.hamcrest.Matchers.*;

public class PostAdminRegistrationApplicationTest extends DataProvider {
    ApplicationRegistration application = new ApplicationRegistration();


    @Test
    public void createAnAdminRegistrationApplication() {
        application = Helper.generateApplicationRegistrationPayload();
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(successResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(dataProvider = "invalidDataForPostApplicationReasonField")
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {
        application = Helper.generateApplicationRegistrationPayloadWithoutReason();
        application.setReason(reason);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId("invalidId");
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("isSuccess", equalTo(false))
                .body("message", containsString("INSTITUTION NOT EXIST!"));
    }

    @Test
    @Story("As a Super Admin when I create an admin registration application with missing institution ID I want to get a proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void createAnAdminRegistrationApplicationWithMissingInstitutionId() {
        application = Helper.generateApplicationRegistrationPayload();
        application.setInstitutionId(null);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("institutionId"))
                .body("subErrors[0].type", equalTo("String"));
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
