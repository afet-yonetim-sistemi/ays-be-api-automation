package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.User;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest {
    String userID;

    @BeforeMethod
    public void setup() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        userID = Helper.extractUserIdByPhoneNumber(user.getPhoneNumber());
    }

    @Test()
    public void deleteUser() {
        Response deleteResponse = InstitutionEndpoints.deleteUser(userID);
        deleteResponse.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

        Response getResponse = InstitutionEndpoints.getUser(userID);
        getResponse.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));

    }


    @Test()
    public void deleteUserNegative() {
        InstitutionEndpoints.deleteUser(userID);
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
