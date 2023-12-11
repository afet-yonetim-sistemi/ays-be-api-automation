package tests.user.userselfmanagementservice;

import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserSelfTest {
    UserCredentials userCredentials;
    User user;
    String userID;
    Logger logger = LogManager.getLogger(this.getClass());
    @BeforeMethod
    public void setup() {
        user=Helper.createUserPayload();
        userCredentials = Helper.createNewUser(user);
        userID = Helper.getUserIDByFirstName(user.getFirstName());
        System.out.println(user.getFirstName());
        user = Helper.getUser(userID);
    }


    @Test()
    public void getSelfInfoPositive(){
        logger.info("Test case UMS_14 is running");
        Response response= UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .contentType("application/json")
                .statusCode(200)
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("time", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response", Matchers.hasKey("email"))
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.supportStatus", notNullValue())
                .body("response.phoneNumber", notNullValue())
                .body("response.institution", notNullValue());

    }
    @Test()
    public void getSelfInfoNegative(){
        logger.info("Test case UMS_15 is running");
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        System.out.println(userID);
        InstitutionEndpoints.updateUser(userID,user);
        Response response=UserEndpoints.getUserSelfInfo(userCredentials.getUsername(), userCredentials.getPassword());
        response.then()
                .statusCode(401)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("UNAUTHORIZED"))
                .body("header", equalTo("AUTH ERROR"))
                .body("isSuccess", equalTo(false));

    }

}
