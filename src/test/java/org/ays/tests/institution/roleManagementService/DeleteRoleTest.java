package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.utility.AysDataProvider;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class DeleteRoleTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteRolPositiveScenario() {
        String roleId = RoleEndpoints.generateRoleId();

        Response response = RoleEndpoints.deleteRole(roleId);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteRolWithNonInstitutionRol() {
        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Disaster Foundation");

        Response response = RoleEndpoints.deleteRole(roleId);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteRolWithDeletedStatus() {
        String roleId = RoleDataSource.findLastDeletedRoleIdByInstitutionName("Test Foundation");

        Response response = RoleEndpoints.deleteRole(roleId);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("role is already deleted!"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = AysDataProvider.class)
    public void deleteRolWithInvalidId(String roleId, AysErrorMessage errorMessage, String field, String type) {

        Response response = RoleEndpoints.deleteRole(roleId);
        response.then()
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

}
