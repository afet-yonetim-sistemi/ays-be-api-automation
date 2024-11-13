package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class RoleCreateTest {

    private String accessToken;

    @BeforeMethod
    private void setUpToken() {
        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        this.accessToken = this.loginAndGetAccessToken(loginPayload);
    }

    @Test(groups = {"Smoke", "Regression"})
    public void createRolePositiveScenario() {

        RoleCreatePayload role = RoleCreatePayload.generate();
        Response response = RoleEndpoints.create(role, accessToken);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void isTheCreatedRoleStatusActive() {

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);

        String status = RoleDataSource.findLastCreatedRoleStatusByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);

        Assert.assertEquals(status, RoleStatus.ACTIVE.toString());
    }

    @Test(groups = {"Regression"})
    public void createRoleWithExistingRoleName() {

        RoleCreatePayload role = RoleCreatePayload.generate();
        String existingRoleName = RoleDataSource.findLastCreatedRoleNameByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);
        role.setName(existingRoleName);

        Response response = RoleEndpoints.create(role, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", equalTo("role already exist! name:" + existingRoleName + ""));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidRoleName", dataProviderClass = AysDataProvider.class)
    public void createRoleWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {

        RoleCreatePayload role = RoleCreatePayload.generate();
        role.setName(name);

        Response response = RoleEndpoints.create(role, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void createRoleWithNullRoleName() {

        RoleCreatePayload role = RoleCreatePayload.generate();
        role.setName(null);

        Response response = RoleEndpoints.create(role, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(AysErrorMessage.MUST_NOT_BE_BLANK.getMessage()));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPermissionIds", dataProviderClass = AysDataProvider.class)
    public void createRoleWithInvalidPermissionIdList(List<String> permissionIds, AysErrorMessage errorMessage,
                                                      String field, String type) {

        RoleCreatePayload role = RoleCreatePayload.generate();
        role.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.create(role, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void createRoleWithSamePermissionIds() {

        RoleCreatePayload role = RoleCreatePayload.generate();
        List<String> samePermissionIds = PermissionDataSource.getTwoSamePermissionIds();
        role.setPermissionIds(samePermissionIds);

        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);

        List<String> permissionIdsFromData = RoleDataSource.getPermissionIdsFromCreatedRole(roleId);

        Assert.assertEquals(permissionIdsFromData.size(), 1, "Permission IDs should be unique in the database.");
        Assert.assertEquals(permissionIdsFromData.get(0), samePermissionIds.get(0),
                "Permission ID in the database should match the one set in the role.");
    }

    @Test(groups = {"Regression"})
    public void createRoleByUserWithoutRoleCreatePermission() {

        LoginPayload login = LoginPayload.generateAsTestVolunteerFoundationUser();
        String token = loginAndGetAccessToken(login);

        RoleCreatePayload role = RoleCreatePayload.generate();

        Response response = RoleEndpoints.create(role, token);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
