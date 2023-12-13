package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class GetUserTest {
    Logger logger = LogManager.getLogger(this.getClass());
    User user=new User();

    @Test()
    public void getUser() {
        logger.info("Test case UMS_26 is running");
        user=Helper.createUserPayload();
        Helper.createNewUser(user);
        String userID;
        userID=Helper.getUserIDByFirstName(user.getFirstName());
        Response response = InstitutionEndpoints.getUser(userID);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("response.createdUser", notNullValue())
                .body("response.id", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.supportStatus", notNullValue());
    }
}
