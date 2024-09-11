package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.common.model.enums.ErrorMessage;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.PhoneNumber;
import org.ays.payload.User;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class PostUserTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void createAUser() {
        User user = User.generate();
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidPhoneNumber(String countryCode, String lineNumber, ErrorMessage errorMessage, String field, String type) {
        User user = User.generate();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        user.setPhoneNumber(phoneNumber);
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidFirstnameAndLastname(String firstName, String lastName, ErrorMessage errorMessage, String field, String type) {
        User user = User.generate();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmail", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidEmailAddress(String emailAddress, ErrorMessage errorMessage, String field, String type) {
        User user = User.generate();
        user.setEmailAddress(emailAddress);
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidIdData", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidRoleId(String id, ErrorMessage errorMessage, String field, String type) {
        User user = User.generate();
        user.setRoleIds(List.of(id));
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForCreateUser", dataProviderClass = DataProvider.class)
    public void createUserWithInvalidCity(String city, ErrorMessage errorMessage, String field, String type) {
        User user = User.generate();
        user.setCity(city);
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createUserWithEmptyRoleList() {
        User user = new User();
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(ErrorMessage.MUST_NOT_BE_EMPTY, "roleIds", "Set"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void createAUserWithOtherInstitutionsRole() {
        User user = User.generate();
        user.setRoleIds(Arrays.asList(DatabaseUtility.getRoleId(AysConfigurationProperty.Database.AFET_YONETIM_SISTEMI_ID)));
        Response response = InstitutionEndpoints.createAUser(user);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("the following roles are not found!"));
    }
}
