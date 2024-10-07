package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.RoleUpdatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RoleUpdateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateRolePositiveScenario() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setName(name);

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRoleWithNullRoleName() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setName(null);

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPermissionIds", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidPermissionIdList(List<String> permissionIds, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRoleWithNullPermissionIds() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setPermissionIds(null);

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be empty"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNonInstitutionRole() {

        LoginPayload loginPayloadForTestAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String testAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForTestAdmin);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, testAdminAccessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();

        LoginPayload loginPayloadForDisasterFoundationAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String disasterFoundationAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForDisasterFoundationAdmin);

        Response response = RoleEndpoints.update(roleId, roleUpdatePayload, disasterFoundationAdminAccessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
