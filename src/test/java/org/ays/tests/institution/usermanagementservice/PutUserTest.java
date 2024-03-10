package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Helper;
import org.ays.payload.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PutUserTest {
    User user = new User();
    String userID;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        user = User.generate();
        InstitutionEndpoints.createAUser(user);
        userID = Helper.extractUserIdByPhoneNumber(user.getPhoneNumber());
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserAsPassive() {
        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
        Assert.assertEquals(user.getStatus(), "PASSIVE");

    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserAsActive() {
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

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRole() {
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

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithBlankRole() {
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

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithBlankStatus() {
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
