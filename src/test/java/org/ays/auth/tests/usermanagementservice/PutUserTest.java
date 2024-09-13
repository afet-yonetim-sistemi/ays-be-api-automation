package org.ays.auth.tests.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserUpdatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PutUserTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserSuccessfully() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setFirstName("updatedName");
        Response response = UserEndpoints.updateUser(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithoutAuthorizationToken() {

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, null);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidUserId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidName(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setFirstName(firstName);
        userUpdatePayload.setLastName(lastName);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidEmail(String emailAddress, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setEmailAddress(emailAddress);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidRoleList(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setRoleIds(List.of(id));
        Response response = UserEndpoints.updateUser(userId, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRoleForInstitution() {

        LoginPayload loginPayloadAdminTwo = LoginPayload.generateAsAdminUserTwo();
        String accessTokenAdminTwo = this.loginAndGetAccessToken(loginPayloadAdminTwo);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);
        RoleEndpoints.createRole(RoleCreatePayload.generate(), accessTokenAdminTwo);

        LoginPayload loginPayloadTestAdmin = LoginPayload.generateAsTestAdmin();
        String accessTokenTestAdmin = this.loginAndGetAccessToken(loginPayloadTestAdmin);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setRoleIds(List.of(RoleDataSource.findLastRoleId()));
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessTokenTestAdmin);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithMissingField() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setLastName(null);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(AysErrorMessage.MUST_NOT_BE_BLANK, "lastName", "String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.getPhoneNumber().setCountryCode(countryCode);
        userUpdatePayload.getPhoneNumber().setLineNumber(lineNumber);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidCity(String city, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String id = UserEndpoints.generateUserId(userCreatePayload);

        UserUpdatePayload userUpdatePayload = UserUpdatePayload.from(userCreatePayload);
        userUpdatePayload.setCity(city);
        Response response = UserEndpoints.updateUser(id, userUpdatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
