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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class RoleCreateTest {

    private String accessToken;

    @BeforeMethod(alwaysRun = true)
    private void setUpToken() {
        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        this.accessToken = this.loginAndGetAccessToken(loginPayload);
    }

    @Test(groups = {"Smoke", "Regression"})
    public void createRolePositiveScenario() {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        Response response = RoleEndpoints.create(roleCreatePayload, accessToken);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void isTheCreatedRoleStatusActive() {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);

        String status = RoleDataSource.findLastCreatedRoleStatusByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);

        Assert.assertEquals(status, RoleStatus.ACTIVE.toString());
    }

    @Test(groups = {"Regression"})
    public void createRoleWithExistingRoleName() {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        String existingRoleName = RoleDataSource.findLastCreatedRoleNameByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);
        roleCreatePayload.setName(existingRoleName);

        Response response = RoleEndpoints.create(roleCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", equalTo("role already exist! name:" + existingRoleName));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidRoleName", dataProviderClass = AysDataProvider.class)
    public void createRoleWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        roleCreatePayload.setName(name);

        Response response = RoleEndpoints.create(roleCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void createRoleWithNullRoleName() {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        roleCreatePayload.setName(null);

        Response response = RoleEndpoints.create(roleCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(AysErrorMessage.MUST_NOT_BE_BLANK.getMessage()));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPermissionIds", dataProviderClass = AysDataProvider.class)
    public void createRoleWithInvalidPermissionIdList(List<String> permissionIds, AysErrorMessage errorMessage,
                                                      String field, String type) {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        roleCreatePayload.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.create(roleCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void createRoleWithSamePermissionIds() {

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        List<String> allPermissions = PermissionDataSource.findAllPermissionIds();
        Collections.shuffle(allPermissions);
        String randomPermissionId = allPermissions.get(0);

        roleCreatePayload.setPermissionIds(List.of(randomPermissionId, randomPermissionId));

        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty
                .TestVolunteerFoundation.ID);

        List<String> rolePermissions = RoleDataSource.findAllPermissionIdsFromCreatedRole(roleId);

        Assert.assertEquals(rolePermissions.size(), 1, "Permission IDs should be unique in the database.");
        Assert.assertEquals(rolePermissions.get(0), randomPermissionId,
                "Permission ID in the database should match the one set in the role.");
    }

    @Test(groups = {"Regression"}, enabled = false)
    public void createRoleByUserWithoutRoleCreatePermission() {

        LoginPayload login = LoginPayload.generateAsTestVolunteerFoundationUser();
        String token = loginAndGetAccessToken(login);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();

        Response response = RoleEndpoints.create(roleCreatePayload, token);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
