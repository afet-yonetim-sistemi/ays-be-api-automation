package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.RoleUpdatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PutRoleTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateRolPositiveScenario() {
        String roleId = RoleEndpoints.generateRoleId();
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(PermissionDataSource.getPermissionsId());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = DataProvider.class)
    public void updateRolWithInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(name);
        roleUpdatePayload.setPermissionIds(PermissionDataSource.getPermissionsId());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNullRoleName() {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(null);
        roleUpdatePayload.setPermissionIds(PermissionDataSource.getPermissionsId());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPermissionIds", dataProviderClass = DataProvider.class)
    public void updateRolWithInvalidPermissionIds(List<String> permissionIds, AysErrorMessage errorMessage, String field, String type) {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(permissionIds);

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNullPermissionIds() {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(null);

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be empty"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNonInstitutionRol() {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Disaster Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(PermissionDataSource.getPermissionsId());

        Response response = RoleEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

}
