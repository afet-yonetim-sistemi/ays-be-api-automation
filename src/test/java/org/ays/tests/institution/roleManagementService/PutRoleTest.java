package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.RoleUpdatePayload;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PutRoleTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateRolPositiveScenario() {
        String roleId = InstitutionEndpoints.generateRoleId();
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(DatabaseUtility.getPermissionsId());

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = DataProvider.class)
    public void updateRolWithInvalidRoleName(String name, ErrorMessage errorMessage, String field, String type) {
        String roleId = DatabaseUtility.getRoleIdForInstitution("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(name);
        roleUpdatePayload.setPermissionIds(DatabaseUtility.getPermissionsId());

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNullRoleName() {
        String roleId = DatabaseUtility.getRoleIdForInstitution("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(null);
        roleUpdatePayload.setPermissionIds(DatabaseUtility.getPermissionsId());

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPermissionIds", dataProviderClass = DataProvider.class)
    public void updateRolWithInvalidPermissionIds(List<String> permissionIds, ErrorMessage errorMessage, String field, String type) {
        String roleId = DatabaseUtility.getRoleIdForInstitution("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(permissionIds);

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNullPermissionIds() {
        String roleId = DatabaseUtility.getRoleIdForInstitution("Test Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(null);

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be empty"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateRolWithNonInstitutionRol() {
        String roleId = DatabaseUtility.getRoleIdForInstitution("Disaster Foundation");
        RoleUpdatePayload roleUpdatePayload = new RoleUpdatePayload();
        roleUpdatePayload.setName(AysRandomUtil.generateFirstName() + " Rol");
        roleUpdatePayload.setPermissionIds(DatabaseUtility.getPermissionsId());

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

}