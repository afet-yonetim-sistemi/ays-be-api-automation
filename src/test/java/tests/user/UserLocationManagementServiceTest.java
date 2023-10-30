package tests.user;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Helper;
import payload.PhoneNumber;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class UserLocationManagementServiceTest extends UserEndpoints {
    Faker faker;
    User userPayload;
    PhoneNumber phoneNumber;
    UserCredentials userCredentials;

    @BeforeClass
    public void setup() {
        faker = new Faker();
        userPayload = new User();
        phoneNumber = new PhoneNumber();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(Helper.generateLineNumber());
        phoneNumber.setCountryCode("90");
        userPayload.setPhoneNumber(phoneNumber);
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
    }

    @Test(priority = 0)
    public void assignmentSearchAsSupportStatusIDLE() {
//        String payload = "{\n" +
//                "    \"latitude\": 37.991739,\n" +
//                "    \"longitude\": 27.024168\n" +
//                "}";
//        Response response = UserEndpoints.searchAssignment(payload, userCredentials.getUsername(), userCredentials.getPassword());
//        response.then()
//                .statusCode(409)
//                .body("time", notNullValue())
//                .body("httpStatus", equalTo("CONFLICT"))
//                .body("header", equalTo("ALREADY EXIST"))
//                .body("message", containsString("USER NOT READY TO TAKE ASSIGNMENT!"))
//                .body("isSuccess", equalTo(false));
    }
}
