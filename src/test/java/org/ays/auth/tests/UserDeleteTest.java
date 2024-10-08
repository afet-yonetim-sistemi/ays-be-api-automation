package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class UserDeleteTest {

    @Test(groups = {"Smoke", "Regression"})
    public void deleteUser() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);

        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        AysPhoneNumber phoneNumber = userCreatePayload.getPhoneNumber();
        String id = UserDataSource.findIdByPhoneNumber(phoneNumber);

        Response deleteResponse = UserEndpoints.delete(id, accessToken);
        deleteResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getResponse = UserEndpoints.findById(id, accessToken);
        getResponse.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));

    }


    @Test(groups = {"Regression"})
    public void deleteUserNegative() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);

        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        AysPhoneNumber phoneNumber = userCreatePayload.getPhoneNumber();
        String id = UserDataSource.findIdByPhoneNumber(phoneNumber);

        UserEndpoints.delete(id, accessToken);
        Response response = UserEndpoints.delete(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("user is already deleted! id:" + id));

    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
