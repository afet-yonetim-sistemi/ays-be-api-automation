package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.common.enums.ErrorMessage;
import org.ays.endpoints.Authorization;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PatchRolePassivateTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void passivateRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        Response response = InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = DataProvider.class)
    public void passivateRoleWithInvalidId(String id, ErrorMessage errorMessage, String field, String type) {
        Response response = InstitutionEndpoints.patchPassivateRole(id, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAPassivatedRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        Response response = InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(ErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAnAssignedRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.createAUser(User.generateUserWithARole(roleId), Authorization.loginAndGetTestAdminAccessToken());
        Response response = InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString(ErrorMessage.THE_ROLE_IS_ASSIGNED.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateADeletedRole() {
        String roleId = InstitutionEndpoints.generateRoleId();
        InstitutionEndpoints.deleteRole(roleId);
        Response response = InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(ErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateRoleInDifferentInstitution() {
        String roleId = InstitutionEndpoints.generateRoleId();
        Response response = InstitutionEndpoints.patchPassivateRole(roleId, Authorization.loginAndGetAdminTwoAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(ErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }


}
