package tests.institution.usermanagement;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import payload.Helper;
import payload.UserCredentials;

import static org.hamcrest.Matchers.*;

public class GetUserServiceTest {
    UserCredentials userCredentials;
    Logger logger = LogManager.getLogger(this.getClass());

    @Test()
    public void getUser() {
        logger.info("Test case UMS_26 is running");
        String userID;
        userCredentials=Helper.createNewUser();
        userID=Helper.getUserID(userCredentials.getUsername());
        Response response = InstitutionEndpoints.getUser(userID);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("response.createdUser", notNullValue())
                .body("response.id", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response", hasKey("email"))
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.supportStatus", notNullValue())
                .body("response.institution.createdUser", notNullValue())
                .body("response.institution.createdAt", notNullValue())
                .body("response.institution.updatedUser", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.institution.updatedAt", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.institution.id", notNullValue())
                .body("response.institution.name", notNullValue());
    }
}
