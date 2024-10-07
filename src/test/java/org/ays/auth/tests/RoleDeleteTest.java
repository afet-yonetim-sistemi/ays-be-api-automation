package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class RoleDeleteTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void deleteRolePositive() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        Response response = RoleEndpoints.delete(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteRoleWithNonInstitutionRole() {

        LoginPayload loginPayloadForTestAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String testAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForTestAdmin);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, testAdminAccessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        LoginPayload loginPayloadForDisasterFoundationAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String disasterFoundationAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForDisasterFoundationAdmin);

        Response response = RoleEndpoints.delete(roleId, disasterFoundationAdminAccessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("role does not exist!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void deleteAnAlreadyDeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleEndpoints.delete(roleId, accessToken);

        Response response = RoleEndpoints.delete(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("role is already deleted!"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void deleteRoleWithInvalidRoleId(String roleId, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.delete(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
