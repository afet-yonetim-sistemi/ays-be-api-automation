package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserUpdatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class UserUpdateTest {

    @Test(groups = {"Smoke", "Regression"})
    public void updateUserSuccessfully() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource
                .findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setFirstName("updatedName");
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void updateUserWithoutAuthorizationToken() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        Response response = UserEndpoints.update(userId, userUpdatePayload, null);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidUserId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        Response response = UserEndpoints.update(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidName(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setFirstName(firstName);
        userUpdatePayload.setLastName(lastName);
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmail", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidEmail(String emailAddress, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setEmailAddress(emailAddress);
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidRoleIdListData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidRoleList(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setRoleIds(List.of(id));
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void updateUserWithNonInstitutionRole() {

        LoginPayload loginPayloadForDisasterFoundationAdmin = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessTokenDisasterFoundationAdmin = this.loginAndGetAccessToken(loginPayloadForDisasterFoundationAdmin);

        RoleCreatePayload roleForDisasterFoundation = RoleCreatePayload.generate();
        RoleEndpoints.create(roleForDisasterFoundation, accessTokenDisasterFoundationAdmin);
        String roleIdForDisasterFoundation =
                RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);


        LoginPayload loginPayloadForTestVolunteerFoundationAdmin = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessTokenTestVolunteerFoundationAdmin = this.loginAndGetAccessToken(loginPayloadForTestVolunteerFoundationAdmin);

        RoleCreatePayload roleForTestVolunteerFoundation = RoleCreatePayload.generate();
        RoleEndpoints.create(roleForTestVolunteerFoundation, accessTokenTestVolunteerFoundationAdmin);
        String roleIdForTestFoundation = RoleDataSource
                .findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);


        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleIdForTestFoundation);
        UserEndpoints.create(user, accessTokenTestVolunteerFoundationAdmin);
        String id = UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setRoleIds(List.of(roleIdForDisasterFoundation));
        Response response = UserEndpoints.update(id, userUpdatePayload, accessTokenTestVolunteerFoundationAdmin);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    @Test(groups = {"Regression"})
    public void updateUserWithMissingField() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setLastName(null);
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(AysErrorMessage.MUST_NOT_BE_BLANK, "lastName", "String"));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.getPhoneNumber().setCountryCode(countryCode);
        userUpdatePayload.getPhoneNumber().setLineNumber(lineNumber);
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidCity(String city, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload role = RoleCreatePayload.generate();
        RoleEndpoints.create(role, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload user = UserCreatePayload.generateUserWithARole(roleId);
        UserEndpoints.create(user, accessToken);
        String userId = UserDataSource
                .findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(user);
        userUpdatePayload.setCity(city);
        Response response = UserEndpoints.update(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
