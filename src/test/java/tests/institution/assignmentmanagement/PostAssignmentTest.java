package tests.institution.assignmentmanagement;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Assignment;
import payload.PhoneNumber;
import java.util.Random;
import static org.hamcrest.Matchers.*;
public class PostAssignmentTest extends InstitutionEndpoints {
    Random random = new Random();
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
        assignment.setLatitude(generateRandomCoordinate(38, 40));
        assignment.setLongitude(generateRandomCoordinate(28, 43));

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
        assignment.getPhoneNumber().setCountryCode("12345678");
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("MUST BE VALID"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumber"));
    }

    @Test
    public void createAssignmentWithInvalidPhoneNumber() {
        assignment.getPhoneNumber().setLineNumber("1");
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", not(equalTo("OK")))
                .body("isSuccess", equalTo(false));
    }

    @Test
    public void createAssignmentWithInvalidLongitude() {
        assignment.setLongitude(2000.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be less than or equal to 180"))
                .body("subErrors[0].field", equalTo("longitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo("2000.0"));
    }

    @Test
    public void createAssignmentWithInvalidLatitude() {
        assignment.setLatitude(100.0);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be less than or equal to 90"))
                .body("subErrors[0].field", equalTo("latitude"))
                .body("subErrors[0].type", equalTo("Double"))
                .body("subErrors[0].value", equalTo("100.0"));
    }

    @Test
    public void createAssignmentWithMissingFirstName() {
        assignment.setFirstName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingLastName() {
        assignment.setLastName(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingDescription() {
        assignment.setDescription(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("description"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test
    public void createAssignmentWithMissingPhoneNumber() {
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumber"));
    }

    @Test
    public void createAssignmentWithMissingLatitude() {
        assignment.setLatitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must be less than or equal to 90", "must be greater than or equal to -90"))
                .body("subErrors.field", hasItems("latitude", "latitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test
    public void createAssignmentWithMissingLongitude() {
        assignment.setLongitude(Double.NaN);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must be less than or equal to 180", "must be greater than or equal to -180"))
                .body("subErrors.field", hasItems("longitude", "longitude"))
                .body("subErrors.type", hasItems("Double", "Double"))
                .body("subErrors.value", hasItems("NaN", "NaN"));
    }

    @Test
    public void createAssignmentWithInvalidDataCombination() {
        assignment.setFirstName(null);
        assignment.setPhoneNumber(null);
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        response.then()
                .statusCode(400)
                .log().body()
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("subErrors.message", hasItems("must not be null", "must not be blank"))
                .body("subErrors.field", hasItems("phoneNumber", "firstName"))
                .body("subErrors.type", hasItems("AysPhoneNumber", "String"));

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
    private double generateRandomCoordinate(int min, int max) {
        return min + (max - min) * random.nextDouble();
    }
}
