package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PutUserTest {
    String userID;

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserAsPassive() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), AdminCredentials.generate());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        user.setStatus("PASSIVE");
        user.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
        Assert.assertEquals(user.getStatus(), "PASSIVE");

    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserAsActive() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), AdminCredentials.generate());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        user.setRole("VOLUNTEER");
        user.setStatus("PASSIVE");
        InstitutionEndpoints.updateUser(userID, user);
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
        Assert.assertEquals(user.getStatus(), "ACTIVE");
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRole() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), AdminCredentials.generate());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        user.setRole("VOL");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithBlankRole() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), AdminCredentials.generate());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        user.setRole("");
        user.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithBlankStatus() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);

        PhoneNumber phoneNumber = user.getPhoneNumber();
        Response userIDResponse = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber), AdminCredentials.generate());
        userID = userIDResponse.jsonPath().getString("response.content[0].id");

        user.setRole("VOLUNTEER");
        user.setStatus("");
        Response response = InstitutionEndpoints.updateUser(userID, user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }
}
