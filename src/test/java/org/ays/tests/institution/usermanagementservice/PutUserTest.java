package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.endpoints.Authorization;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PutUserTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserSuccessfully() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setFirstName("updatedName");
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithoutAuthorizationToken() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, null);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidUserId(String id, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        Response response = UserEndpoints.updateUser(id, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidName(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setFirstName(firstName);
        userCreatePayload.setLastName(lastName);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidEmail(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setEmailAddress(emailAddress);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidRoleList(String id, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setRoleIds(List.of(id));
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRoleForInstitution() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        RoleEndpoints.createRole(RoleCreatePayload.generate(), Authorization.loginAndGetAdminTwoAccessToken());
        userCreatePayload.setRoleIds(List.of(RoleDataSource.findLastRoleId()));
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithMissingField() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setLastName(null);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(AysErrorMessage.MUST_NOT_BE_BLANK, "lastName", "String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.getPhoneNumber().setCountryCode(countryCode);
        userCreatePayload.getPhoneNumber().setLineNumber(lineNumber);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = AysDataProvider.class)
    public void updateUserWithInvalidCity(String city, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(userCreatePayload);
        userCreatePayload.setCity(city);
        Response response = UserEndpoints.updateUser(userId, userCreatePayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }


}
