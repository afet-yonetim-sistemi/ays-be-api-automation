package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.RoleUpdatePayload;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class PutRoleTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateRolPositiveScenario() {
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        String roleId = InstitutionEndpoints.generateRoleId();
        RoleUpdatePayload roleUpdatePayload = RoleUpdatePayload.generate(roleCreatePayload);

        Response response = InstitutionEndpoints.updateRole(roleId, roleUpdatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }
}
