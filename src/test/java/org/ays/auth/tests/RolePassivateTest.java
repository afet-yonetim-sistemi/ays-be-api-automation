package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RolePassivateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void passivateRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.createRole(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestFoundation.ID);

        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void passivateRoleWithInvalidRoleId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.updatePassivateRole(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAPassivatedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.createRole(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestFoundation.ID);

        RoleEndpoints.updatePassivateRole(roleId, accessToken);

        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAnAssignedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.createRole(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.createAUser(user, accessToken);

        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString(AysErrorMessage.THE_ROLE_IS_ASSIGNED.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateAnAlreadyDeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.createRole(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestFoundation.ID);

        RoleEndpoints.deleteRole(roleId, accessToken);

        Response response = RoleEndpoints.updatePassivateRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_ACTIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void passivateRoleInDifferentInstitution() {
        LoginPayload loginPayloadForTestAdmin = LoginPayload.generateAsTestFoundationAdmin();
        String testAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForTestAdmin);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.createRole(role, testAdminAccessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestFoundation.ID);

        LoginPayload loginPayloadForDisasterFoundationAdmin = LoginPayload.generateAsDisasterFoundationAdmin();
        String disasterFoundationAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForDisasterFoundationAdmin);

        Response response = RoleEndpoints.updatePassivateRole(roleId, disasterFoundationAdminAccessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(AysErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
