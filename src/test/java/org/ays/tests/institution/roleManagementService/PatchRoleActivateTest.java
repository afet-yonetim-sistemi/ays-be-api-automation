package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.endpoints.Authorization;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PatchRoleActivateTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void activateRole() {
        String roleId = RoleEndpoints.generateRoleId();
        RoleEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        Response response = RoleEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = AysDataProvider.class)
    public void activateRoleWithInvalidId(String id, AysErrorMessage errorMessage, String field, String type) {
        Response response = RoleEndpoints.patchActivateRole(id, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnActiveRole() {
        String roleId = RoleEndpoints.generateRoleId();
        Response response = RoleEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"}, enabled = true)
    public void activateAnAssignedRole() {
        String roleId = RoleEndpoints.generateRoleId();
        UserEndpoints.createAUser(UserCreatePayload.generateUserWithARole(roleId));
        Response response = RoleEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateADeletedRole() {
        String roleId = RoleEndpoints.generateRoleId();
        RoleEndpoints.deleteRole(roleId);
        Response response = RoleEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateRoleInDifferentInstitution() {
        String roleId = RoleEndpoints.generateRoleId();
        RoleEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetAdminTwoAccessToken());
        Response response = RoleEndpoints.patchActivateRole(roleId, Authorization.loginAndGetAdminTwoAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(AysErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }
}
