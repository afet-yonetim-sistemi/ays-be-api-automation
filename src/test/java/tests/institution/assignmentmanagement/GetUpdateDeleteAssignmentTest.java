package tests.institution.assignmentmanagement;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.Pagination;
import payload.PhoneNumber;
import payload.RequestBodyAssignments;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.*;

public class GetUpdateDeleteAssignmentTest extends InstitutionEndpoints {
    Faker faker;
    Assignment assignment;
    PhoneNumber phoneNumber;
    RequestBodyAssignments requestBodyAssignments;
    String assignmentId;

    @BeforeClass
    public void setupData() {
        int MIN_LATITUDE = 35;
        int MAX_LATITUDE = 42;
        int MIN_LONGITUDE = 26;
        int MAX_LONGITUDE = 45;
        faker = new Faker();
        assignment = new Assignment();
        phoneNumber = new PhoneNumber();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        assignment.setPhoneNumber(phoneNumber);
        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(faker.number().randomDouble(6, MIN_LATITUDE, MAX_LATITUDE));
        assignment.setLongitude(faker.number().randomDouble(6, MIN_LONGITUDE, MAX_LONGITUDE));
        requestBodyAssignments = new RequestBodyAssignments();
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);
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
    public void listAssignment() {
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200);
        assignmentId = extractIdFromListAssignments(response);
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
                .statusCode(404) // Assuming you expect a 404 status code
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("header", equalTo("NOT EXIST"))
                .body("message", containsString("ASSIGNMENT NOT EXIST!"))
                .body("isSuccess", equalTo(false));
    }

    public String extractIdFromListAssignments(Response response) {
        String id = null;
        String latestCreatedAt = "";
        List<Map<String, Object>> content = response.jsonPath().getList("response.content");
        for (Map<String, Object> assignment : content) {
            String createdAt = (String) assignment.get("createdAt");
            if (createdAt != null && createdAt.compareTo(latestCreatedAt) > 0) {
                latestCreatedAt = createdAt;
                id = (String) assignment.get("id");
            }
        }
        return id;
    }

    public static String generateLineNumber() {
        Random random = new Random();
        StringBuilder phoneNumberBuilder = new StringBuilder();
        phoneNumberBuilder.append("5");
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            phoneNumberBuilder.append(digit);
        }
        return phoneNumberBuilder.toString();

    }

}
