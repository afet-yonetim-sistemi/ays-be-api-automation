package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.role.enpoints.RoleEndpoints;
import org.ays.auth.user.endpoints.UserEndpoints;
import org.ays.common.model.enums.ErrorMessage;
import org.ays.endpoints.Authorization;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PutUserTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserSuccessfully() {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setFirstName("updatedName");
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithoutAuthorizationToken() {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        Response response = UserEndpoints.updateUser(userId, user, null);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidUserId(String id, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        Response response = UserEndpoints.updateUser(id, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidName(String firstName, String lastName, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidEmail(String emailAddress, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setEmailAddress(emailAddress);
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidRoleList(String id, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setRoleIds(List.of(id));
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRoleForInstitution() {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        RoleEndpoints.createRole(RoleCreatePayload.generate(), Authorization.loginAndGetAdminTwoAccessToken());
        user.setRoleIds(List.of(DatabaseUtility.getLastCreatedRoleId()));
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithMissingField() {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setLastName(null);
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(ErrorMessage.MUST_NOT_BE_BLANK, "lastName", "String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidPhoneNumber(String countryCode, String lineNumber, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.getPhoneNumber().setCountryCode(countryCode);
        user.getPhoneNumber().setLineNumber(lineNumber);
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidCity(String city, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(RoleEndpoints.generateRoleId());
        String userId = UserEndpoints.generateUserId(user);
        user.setCity(city);
        Response response = UserEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }


}
