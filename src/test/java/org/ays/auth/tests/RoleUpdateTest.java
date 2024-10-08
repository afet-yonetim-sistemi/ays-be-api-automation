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

    @Test(groups = {"Smoke", "Regression"})
    public void updateRolePositiveScenario() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidRoleName", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setName(name);

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void updateRoleWithNullRoleName() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setName(null);

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPermissionIds", dataProviderClass = AysDataProvider.class)
    public void updateRoleWithInvalidPermissionIdList(List<String> permissionIds, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void updateRoleWithNullPermissionIds() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();
        roleUpdatePayload.setPermissionIds(null);

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be empty"));
    }

    @Test(groups = {"Regression"})
    public void updateRolWithNonInstitutionRole() {

        LoginPayload loginPayloadForDisasterFoundationAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessTokenForDisasterFoundationAdmin = this.loginAndGetAccessToken(loginPayloadForDisasterFoundationAdmin);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessTokenForDisasterFoundationAdmin);
        String id = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate();

        LoginPayload loginPayloadForVolunteerFoundationAdmin = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessTokenForVolunteerFoundationAdmin = this.loginAndGetAccessToken(loginPayloadForVolunteerFoundationAdmin);

        Response response = RoleEndpoints.update(id, roleUpdatePayload, accessTokenForVolunteerFoundationAdmin);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist! id:" + id));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
