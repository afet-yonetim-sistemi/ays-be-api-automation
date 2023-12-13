package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest {
    User user;
    String userID;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public void setup() {
        user=Helper.createUserPayload();
        Helper.createNewUser(user);
        userID=Helper.getUserIDByFirstName(user.getFirstName());
    }

    @Test()
    public void deleteUser() {
        logger.info("Test case UMS_36 is running");
        Response deleteResponse = InstitutionEndpoints.deleteUser(userID);
        deleteResponse.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

        logger.info("Test case UMS_39 is running");
        Response getResponse = InstitutionEndpoints.getUser(userID);
        getResponse.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));

    }


    @Test()
    public void deleteUserNegative() {
        logger.info("Test case UMS_37 is running");
        Response response = InstitutionEndpoints.deleteUser(userID);
        response.then()
                .statusCode(409)
                .contentType("application/json")
                .body("httpStatus", equalTo("CONFLICT"))
                .body("isSuccess", equalTo(false))
                .body("header", containsString("ALREADY EXIST"))
                .body("message", containsString("USER IS ALREADY DELETED!"));

    }

}
