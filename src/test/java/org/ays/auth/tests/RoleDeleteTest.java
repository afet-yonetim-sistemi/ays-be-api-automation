package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class RoleDeleteTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteRolePositive() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleEndpoints.createAndReturnRoleId(RoleCreatePayload.generate(),accessToken);

        Response response = RoleEndpoints.deleteRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteRoleWithNonInstitutionRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastRoleIdByInstitutionName("Disaster Foundation");

        Response response = RoleEndpoints.deleteRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteAnAlreadyDeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String roleId = RoleDataSource.findLastDeletedRoleIdByInstitutionName("Test Foundation");

        Response response = RoleEndpoints.deleteRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("role is already deleted!"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void deleteRoleWithInvalidRoleId(String roleId, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.deleteRole(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
