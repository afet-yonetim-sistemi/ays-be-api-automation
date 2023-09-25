package tests.institution.assignmentmanagement;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.PhoneNumber;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class PostAssignmentTest extends InstitutionEndpoints {
    Faker faker;
    Assignment assignment;
    PhoneNumber phoneNumber;

    @BeforeMethod
    public void setupData() {
        faker = new Faker();
        assignment = new Assignment();
        phoneNumber = new PhoneNumber();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        assignment.setPhoneNumber(phoneNumber);
        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(faker.number().randomDouble(6, -90, 90));
        assignment.setLongitude(faker.number().randomDouble(6, -180, 180));
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

    @Test
    public void createAssignmentWithInvalidCountryCode() {
        assignment.getPhoneNumber().setCountryCode("1234567");
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithInvalidPhoneNumber() {
        assignment.getPhoneNumber().setLineNumber("12345");
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithInvalidLongitude() {
        assignment.setLongitude(2000.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithInvalidLatitude() {
        assignment.setLatitude(100.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingFirstName() {
        assignment.setFirstName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingLastName() {
        assignment.setLastName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingDescription() {
        assignment.setDescription(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingPhoneNumber() {
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingLatitude() {
        //assignment.setLatitude(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithMissingLongitude() {
        //assignment.setLongitude(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithDuplicatePhoneNumber() {
        Response response1 = InstitutionEndpoints.createAnAssignment(assignment);
        response1.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

        // Create another assignment with the same phone number
        Response response2 = InstitutionEndpoints.createAnAssignment(assignment);
        response2.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithInvalidDataCombination() {
        assignment.setFirstName(null);
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(not(equalTo(200)))
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
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
