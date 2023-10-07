package tests.institution.assignmentmanagement;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Helper;
import payload.PhoneNumber;
import payload.RequestBodyAssignments;
import static org.hamcrest.Matchers.*;
public class GetUpdateDeleteAssignmentTest extends InstitutionEndpoints {
    Assignment assignment;
    PhoneNumber phoneNumber;
    RequestBodyAssignments requestBodyAssignments;
    String assignmentId;

    @BeforeClass
    public void setupData() {
        assignment = Helper.createRandomAssignment();
        requestBodyAssignments = Helper.createRequestBodyAssignments();
    }

    @Test(priority = 1)
    public void createAnAssignment() {
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 2)
    public void listAssignments() {
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200);
        assignmentId = Helper.extractIdFromListAssignments(response);
    }

    @Test(priority = 3)
    public void getAssignmentPositive() {
        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("response.createdAt", notNullValue())
                .body("response.id", notNullValue())
                .body("response.description", notNullValue())
                .body("response.status", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.location.longitude", notNullValue())
                .body("response.location.latitude", notNullValue())
                .body("response.user", anyOf(nullValue(), notNullValue()))
                .extract().jsonPath().get("response.firstName");
        assignment.setDescription(response.path("response.description"));
        assignment.setFirstName(response.path("response.firstName"));
        assignment.setLastName(response.path("response.lastName"));
        phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(response.path("response.phoneNumber.countryCode"));
        phoneNumber.setLineNumber(response.path("response.phoneNumber.lineNumber"));
        assignment.setPhoneNumber(phoneNumber);
        assignment.setLatitude(response.path("response.location.latitude"));
        assignment.setLongitude(response.path("response.location.longitude"));
    }

    @Test(priority = 4)
    public void updateAssignmentFirstname() {
        String updatedName = "updated firstname";
        assignment.setFirstName(updatedName);
        Response response = InstitutionEndpoints.updateAssignment(assignmentId, assignment);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

    }

    @Test(priority = 5)
    public void deleteAssignment() {
        Response response = InstitutionEndpoints.deleteAssignment(assignmentId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 6)
    public void getAssignmentAfterDelete() {
        Response response = InstitutionEndpoints.getAssignment(assignmentId);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }

    @Test(priority = 7)
    public void deleteAssignmentNegative() {
        Response response = InstitutionEndpoints.deleteAssignment(assignmentId);
        response.then()
                .statusCode(404)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }


}
