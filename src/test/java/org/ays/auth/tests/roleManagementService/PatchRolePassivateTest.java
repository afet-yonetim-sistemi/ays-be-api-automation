package org.ays.auth.tests.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PatchRolePassivateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void passivateRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.generateRoleId();
        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = AysDataProvider.class)
    public void passivateRoleWithInvalidId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.updatePassivateRole(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAPassivatedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.generateRoleId();
        RoleEndpoints.updatePassivateRole(roleId, accessToken);
        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAnAssignedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.generateRoleId();
        UserEndpoints.createAUser(UserCreatePayload.generateUserWithARole(roleId), accessToken);
        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString(AysErrorMessage.THE_ROLE_IS_ASSIGNED.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateADeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.generateRoleId();
        RoleEndpoints.deleteRole(roleId);
        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateRoleInDifferentInstitution() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserTwo();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.generateRoleId();
        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(AysErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
