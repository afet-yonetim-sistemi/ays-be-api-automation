package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.model.enums.Permission;
import org.ays.auth.model.enums.SourcePage;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.PasswordCreatePayload;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class PasswordCreateTest {

    @Test(groups = {"Smoke", "Regression"})
    public void createPasswordFirstTime() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        List<String> permissionsIds = Collections.singletonList(PermissionDataSource
                .findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission()));

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(permissionsIds);
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);
        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        String passwordId = UserDataSource.findPasswordIdByUserId(userId);
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        loginPayload.setEmailAddress(userCreatePayload.getEmailAddress());
        loginPayload.setPassword(passwordCreatePayload.getPassword());
        loginPayload.setSourcePage(SourcePage.INSTITUTION);

        Response loginCheckResponse = AuthEndpoints.token(loginPayload);
        loginCheckResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void createPasswordAfterForgotPasswordCalling() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        List<String> permissionsIds = Collections.singletonList(PermissionDataSource
                .findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission()));

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(permissionsIds);
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String emailAddress = userCreatePayload.getEmailAddress();
        passwordForgotPayload.setEmailAddress(emailAddress);
        AuthEndpoints.forgotPassword(passwordForgotPayload);

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        String passwordId = UserDataSource.findPasswordIdByUserId(userId);
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        loginPayload.setEmailAddress(emailAddress);
        loginPayload.setPassword(passwordCreatePayload.getPassword());
        loginPayload.setSourcePage(SourcePage.INSTITUTION);

        Response loginCheckResponse = AuthEndpoints.token(loginPayload);
        loginCheckResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void createPasswordUsingInvalidPasswordId(String passwordId, AysErrorMessage errorMessage, String field, String type) {
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPassword", dataProviderClass = AysDataProvider.class)
    public void createPasswordWithInvalidPasswordData(String password, AysErrorMessage errorMessage, String field, String type) {
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        passwordCreatePayload.setPassword(password);

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        String passwordId = UserDataSource.findPasswordIdByUserId(userId);
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPasswordRepeat", dataProviderClass = AysDataProvider.class)
    public void createPasswordWithInvalidPasswordRepeatData(String password, AysErrorMessage errorMessage, String field, String type) {
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        passwordCreatePayload.setPasswordRepeat(password);

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        String passwordId = UserDataSource.findPasswordIdByUserId(userId);
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void createPasswordWithMismatchedPasswordAndPasswordRepeatData() {
        PasswordCreatePayload passwordCreatePayload = PasswordCreatePayload.generate();
        passwordCreatePayload.setPasswordRepeat(AysRandomUtil.generatePassword());

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        String passwordId = UserDataSource.findPasswordIdByUserId(userId);
        Response response = AuthEndpoints.createPassword(passwordId, passwordCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(AysErrorMessage.PASSWORDS_MUST_BE_EQUAL.getMessage()));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
