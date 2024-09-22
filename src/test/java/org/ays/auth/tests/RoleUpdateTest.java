package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.RoleUpdatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RoleUpdateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateRolePositiveScenario() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(),accessToken);
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(name);
        roleUpdatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRoleWithNullRoleName() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(null);
        roleUpdatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPermissionIds", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidPermissionIdList(List<String> permissionIds, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRoleWithNullPermissionIds() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(null);

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be empty"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNonInstitutionRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Disaster Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(PermissionDataSource.findRandomPermissionIdsAsRoleManagementCategory());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
