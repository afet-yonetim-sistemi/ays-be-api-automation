package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class UserCreateTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void createAUser() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void createUserWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        userCreatePayload.setPhoneNumber(phoneNumber);
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void createUserWithInvalidFirstnameAndLastname(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setFirstName(firstName);
        userCreatePayload.setLastName(lastName);
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = AysDataProvider.class)
    public void createUserWithInvalidEmailAddress(String emailAddress, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setEmailAddress(emailAddress);
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleIdListData", dataProviderClass = AysDataProvider.class)
    public void createUserWithInvalidRoleId(String id, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setRoleIds(List.of(id));
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = AysDataProvider.class)
    public void createUserWithInvalidCity(String city, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setCity(city);
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createUserWithEmptyRoleList() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = new UserCreatePayload();
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(AysErrorMessage.MUST_NOT_BE_EMPTY, "roleIds", "Set"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createAUserWithOtherInstitutionsRole() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setRoleIds(
                List.of(RoleDataSource.findRoleIdByInstitutionId(AysConfigurationProperty.Database.AFET_YONETIM_SISTEMI_ID))
        );
        Response response = UserEndpoints.createAUser(userCreatePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
