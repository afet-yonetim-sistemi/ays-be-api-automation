package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;

import static org.hamcrest.Matchers.equalTo;

public class PutUserTest {
    User user = new User();
    String userID;
    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod
    public void setup() {
        user = Helper.createUserPayload();
        Helper.createNewUser(user);
        userID = Helper.getUserIDByFirstName(user.getFirstName());
    }

    @Test()
    public void updateUserAsPassive() {
        logger.info("Test case UMS_29 is running");
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertEquals(user.getStatus(), "PASSIVE");

    }

    @Test()
    public void updateUserAsActive() {
        logger.info("Test case UMS_35 is running");
        user.setRole("VOLUNTEER");
        user.setStatus("PASSIVE");
        InstitutionEndpoints.updateUser(userID, user);
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertEquals(user.getStatus(), "ACTIVE");
    }

    @Test()
    public void updateUserWithInvalidRole() {
        logger.info("Test case UMS_31 is running");
        user.setRole("VOL");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void updateUserWithBlankRole() {
        logger.info("Test case UMS_32 is running");
        user.setRole("");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @Test()
    public void updateUserWithBlankStatus() {
        logger.info("Test case UMS_34 is running");
        user.setRole("VOLUNTEER");
        user.setStatus("");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }
}
