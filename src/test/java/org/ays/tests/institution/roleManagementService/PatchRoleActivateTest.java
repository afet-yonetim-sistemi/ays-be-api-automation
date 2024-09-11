package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.user.endpoints.UserEndpoints;
import org.ays.common.model.enums.ErrorMessage;
import org.ays.endpoints.Authorization;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PatchRoleActivateTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void activateRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        Response response = InstitutionEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = DataProvider.class)
    public void activateRoleWithInvalidId(String id, ErrorMessage errorMessage, String field, String type) {
        Response response = InstitutionEndpoints.patchActivateRole(id, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnActiveRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        Response response = InstitutionEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(ErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"}, enabled = true)
    public void activateAnAssignedRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        UserEndpoints.createAUser(User.generateUserWithARole(roleId));
        Response response = InstitutionEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(ErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateADeletedRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.deleteRole(roleId);
        Response response = InstitutionEndpoints.patchActivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(ErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateRoleInDifferentInstitution() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetAdminTwoAccessToken());
        Response response = InstitutionEndpoints.patchActivateRole(roleId, Authorization.loginAndGetAdminTwoAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(ErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }
}
