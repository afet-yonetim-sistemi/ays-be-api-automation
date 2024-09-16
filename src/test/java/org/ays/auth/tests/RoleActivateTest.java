package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RoleActivateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void activateRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        String roleId = RoleEndpoints.createAndReturnRoleId(role, accessToken);

        RoleEndpoints.updatePassivateRole(roleId, accessToken);

        Response response = RoleEndpoints.updateActivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void activateRoleWithInvalidId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.updateActivateRole(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnActiveRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(), accessToken);
        Response response = RoleEndpoints.updateActivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnAssignedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(), accessToken);
        UserEndpoints.createAUser(UserCreatePayload.generateUserWithARole(roleId), accessToken);

        Response response = RoleEndpoints.updateActivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateADeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(), accessToken);
        RoleEndpoints.deleteRole(roleId, accessToken);

        Response response = RoleEndpoints.updateActivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateRoleInDifferentInstitution() {

        LoginPayload loginPayloadForTestAdmin = LoginPayload.generateAsTestAdmin();
        String testAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForTestAdmin);
        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(), testAdminAccessToken);
        RoleEndpoints.updatePassivateRole(roleId, testAdminAccessToken);

        LoginPayload loginPayloadForAdminTwo = LoginPayload.generateAsAdminUserTwo();
        String adminTwoAccessToken = this.loginAndGetAccessToken(loginPayloadForAdminTwo);

        Response response = RoleEndpoints.updateActivateRole(roleId, adminTwoAccessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(AysErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
