package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;

public class PasswordForgotTest {

    @Test(groups = {"Smoke", "Regression"})
    public void forgotPasswordPositiveTest() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String emailAddress = userCreatePayload.getEmailAddress();
        passwordForgotPayload.setEmailAddress(emailAddress);

        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void forgotPasswordNegativeTest(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        passwordForgotPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Smoke", "Regression"})
    public void forgotPasswordVerifyTimeChangeInResponse() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String emailAddress = userCreatePayload.getEmailAddress();
        passwordForgotPayload.setEmailAddress(emailAddress);

        Response firstResponse = AuthEndpoints.forgotPassword(passwordForgotPayload);
        firstResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response secondResponse = AuthEndpoints.forgotPassword(passwordForgotPayload);
        secondResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        String firstResponseTime = firstResponse.jsonPath().getString("time");
        String secondResponseTime = secondResponse.jsonPath().getString("time");

        assertNotEquals(firstResponseTime, secondResponseTime,
                "The time value is the same in the first and second response.");

    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
