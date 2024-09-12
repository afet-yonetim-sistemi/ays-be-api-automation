package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PostUserTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void createAUser() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        userCreatePayload.setPhoneNumber(phoneNumber);
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidFirstnameAndLastname(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setFirstName(firstName);
        userCreatePayload.setLastName(lastName);
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidEmailAddress(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setEmailAddress(emailAddress);
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidRoleId(String id, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setRoleIds(List.of(id));
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidCity(String city, AysErrorMessage errorMessage, String field, String type) {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setCity(city);
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createUserWithEmptyRoleList() {
        UserCreatePayload userCreatePayload = new UserCreatePayload();
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(AysErrorMessage.MUST_NOT_BE_EMPTY, "roleIds", "Set"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createAUserWithOtherInstitutionsRole() {
        UserCreatePayload userCreatePayload = UserCreatePayload.generate();
        userCreatePayload.setRoleIds(
                List.of(RoleDataSource.findRoleIdByInstitutionId(AysConfigurationProperty.Database.AFET_YONETIM_SISTEMI_ID))
        );
        Response response = UserEndpoints.createAUser(userCreatePayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }
}
