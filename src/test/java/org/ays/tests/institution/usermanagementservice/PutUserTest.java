package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.Authorization;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.User;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PutUserTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void updateUserSuccessfully() {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setFirstName("updatedName");
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithoutAuthorizationToken() {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        Response response = InstitutionEndpoints.updateUser(userId, user, null);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleId", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidUserId(String id, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        Response response = InstitutionEndpoints.updateUser(id, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidName(String firstName, String lastName, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidEmail(String emailAddress, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setEmailAddress(emailAddress);
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidRoleList(String id, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setRoleIds(List.of(id));
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithInvalidRoleForInstitution() {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        InstitutionEndpoints.createRole(RoleCreatePayload.generate(), Authorization.loginAndGetAdminTwoAccessToken());
        user.setRoleIds(List.of(DatabaseUtility.getLastCreatedRoleId()));
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void updateUserWithMissingField() {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setLastName(null);
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(ErrorMessage.MUST_NOT_BE_BLANK, "lastName", "String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidPhoneNumber(String countryCode, String lineNumber, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.getPhoneNumber().setCountryCode(countryCode);
        user.getPhoneNumber().setLineNumber(lineNumber);
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }
    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = DataProvider.class)
    public void updateUserWithInvalidCity(String city, ErrorMessage errorMessage, String field, String type) {
        User user = User.generateUserWithARole(InstitutionEndpoints.generateRoleId());
        String userId = InstitutionEndpoints.generateUserId(user);
        user.setCity(city);
        Response response = InstitutionEndpoints.updateUser(userId, user, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }


}
