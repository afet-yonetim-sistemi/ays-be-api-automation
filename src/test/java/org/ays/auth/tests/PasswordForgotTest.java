package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.model.enums.Permission;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertNotEquals;

public class PasswordForgotTest {
    @Test(groups = {"Smoke", "Regression"})
    public void forgotPasswordForUsersHavingInstitutionPagePermissions() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        List<String> permissionsIds = Collections.singletonList(PermissionDataSource.findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission()));
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(permissionsIds);
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

    @Test(groups = {"Regression"})
    public void forgotPasswordForUsersWithoutInstitutionPagePermission() {
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
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void forgotPasswordWithInvalidEmailData(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
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

        List<String> permissionsIds = Collections.singletonList(PermissionDataSource.findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission()));
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(permissionsIds);
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

    @Test(groups = {"Regression"})
    public void forgotPasswordForActiveUsers() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionPermission = PermissionDataSource.findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission());
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(List.of(institutionPermission));
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

    @Test(groups = {"Regression"})
    public void forgotPasswordForNotVerifiedUsers() {
        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationCreatePayload application = AdminRegistrationApplicationCreatePayload
                .generate(AysConfigurationProperty.TestVolunteerFoundation.ID, AysRandomUtil.generateReasonString());
        AdminRegistrationApplicationEndpoints.create(application, accessToken);

        String applicationId = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload applicationCompletePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints.complete(applicationId, applicationCompletePayload, accessToken);

        String notVerifiedUserEmailAddress = applicationCompletePayload.getEmailAddress();

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        passwordForgotPayload.setEmailAddress(notVerifiedUserEmailAddress);

        String userStatus = UserDataSource
                .findLastCreatedUserStatusByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        Assert.assertEquals(userStatus, UserStatus.NOT_VERIFIED.name());

        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void forgotPasswordForPassiveUsers() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionPermission = PermissionDataSource.findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission());

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(List.of(institutionPermission));
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);
        UserEndpoints.passivate(userId, accessToken);

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String passiveUserEmailAddress = userCreatePayload.getEmailAddress();
        passwordForgotPayload.setEmailAddress(passiveUserEmailAddress);

        String userStatus = UserDataSource
                .findLastCreatedUserStatusByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        Assert.assertEquals(userStatus, UserStatus.PASSIVE.name());

        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void forgotPasswordForDeletedUsers() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionPermission = PermissionDataSource.findPermissionIdByName(Permission.INSTITUTION_PAGE.getPermission());

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate(List.of(institutionPermission));
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(userCreatePayload, accessToken);

        String userId = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);
        UserEndpoints.delete(userId, accessToken);

        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String passiveUserEmailAddress = userCreatePayload.getEmailAddress();
        passwordForgotPayload.setEmailAddress(passiveUserEmailAddress);

        String userStatus = UserDataSource
                .findLastCreatedUserStatusByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        Assert.assertEquals(userStatus, UserStatus.DELETED.name());

        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
