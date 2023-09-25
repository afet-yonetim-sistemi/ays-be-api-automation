package tests.institution.assignmentmanagement;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payload.Assignment;

import static org.hamcrest.Matchers.*;

public class GetUpdateDeleteAssignmentTest extends InstitutionEndpoints {
    private String assignmentId = "04bf5c5a-d886-475a-b67b-17b3b9fb3177";
    Assignment assignment;

    @Test
    public void getAssignment() {
        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.createdUser", notNullValue())
                .body("response.createdAt", notNullValue())
                .body("response.updatedUser", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.updatedAt", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.id", notNullValue())
                .body("response.description", notNullValue())
                .body("response.status", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.location.longitude", notNullValue())
                .body("response.location.latitude", notNullValue())
                .body("response", hasKey("user"));
        assignment.setLongitude(response.jsonPath().getDouble("response.location.longitude"));
        assignment.setLatitude(response.jsonPath().getDouble("response.location.latitude"));
        assignment.setDescription(response.jsonPath().getString("response.description"));
        assignment.setFirstName(response.jsonPath().getString("response.firstName"));


    }

}
