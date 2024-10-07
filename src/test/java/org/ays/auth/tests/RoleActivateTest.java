package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RoleActivateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void activateRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleEndpoints.passivate(roleId, accessToken);

        Response response = RoleEndpoints.activate(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void activateRoleWithInvalidId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = RoleEndpoints.activate(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnActiveRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        Response response = RoleEndpoints.activate(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateAnAssignedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserEndpoints.create(UserCreatePayload.generateUserWithARole(roleId), accessToken);

        Response response = RoleEndpoints.activate(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateADeletedRole() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleEndpoints.delete(roleId, accessToken);

        Response response = RoleEndpoints.activate(roleId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", equalTo(AysErrorMessage.ROLE_STATUS_IS_NOT_PASSIVE.getMessage()));
    }

    @Test(groups = {"Regression", "Institution"})
    public void activateRoleInDifferentInstitution() {

        LoginPayload loginPayloadForTestAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String testAdminAccessToken = this.loginAndGetAccessToken(loginPayloadForTestAdmin);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, testAdminAccessToken);

        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        RoleEndpoints.passivate(roleId, testAdminAccessToken);

        LoginPayload loginPayloadForTestVolunteerFoundationAdmin = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessTokenForTestVolunteerFoundationAdmin = this.loginAndGetAccessToken(loginPayloadForTestVolunteerFoundationAdmin);

        Response response = RoleEndpoints.activate(roleId, accessTokenForTestVolunteerFoundationAdmin);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString(AysErrorMessage.ROLE_DOES_NOT_EXIST.getMessage()));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
