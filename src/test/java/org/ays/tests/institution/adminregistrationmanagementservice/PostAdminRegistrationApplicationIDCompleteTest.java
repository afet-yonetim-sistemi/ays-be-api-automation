package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyForRegistrationIDComplete;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class PostAdminRegistrationApplicationIDCompleteTest {

    String applicationID;
    RequestBodyForRegistrationIDComplete registrationIDComplete;


    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to create an admin registration complete.")
    @Severity(SeverityLevel.NORMAL)
    public void createRegistrationIDCompletePositive() {
        applicationID = InstitutionEndpoints.generateApplicationID();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    @Story("As a super admin I want to get proper error message when I use invalid firstName information")
    public void createRegistrationIDCompleteWitInvalidFirstName(String invalidFirstName, String errorMessage) {
        applicationID = InstitutionEndpoints.generateApplicationID();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();
        registrationIDComplete.setFirstName(invalidFirstName);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    @Story("As a super admin I want to get proper error message when I use lastName ID information")
    public void createRegistrationIDCompleteWitInvalidLastName(String invalidFLastName, String errorMessage) {
        applicationID = InstitutionEndpoints.generateApplicationID();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();
        registrationIDComplete.setLastName(invalidFLastName);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidEmailForAdminRegistration", dataProviderClass = DataProvider.class)
    @Story("As a super admin I want to get proper error message when I use invalid email information.")
    public void createRegistrationIDCompleteWitInvalidEmail(String invalidEmail, String errorMessage) {
        applicationID = InstitutionEndpoints.generateApplicationID();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();
        registrationIDComplete.setEmail(invalidEmail);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("email"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidPhoneNumberDataForRegistrationComplete", dataProviderClass = DataProvider.class)
    @Story("As a super admin I want to get proper error message when I use invalid phoneNumber information.")
    public void createRegistrationIDCompleteWitInvalidPhoneNumber(String countryCode, String lineNumber, String errorMessage, String field, String type) {
        applicationID = InstitutionEndpoints.generateApplicationID();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        registrationIDComplete.setPhoneNumber(phoneNumber);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void createRegistrationWithInvalidFormatID() {
        applicationID = "invalidID";
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I use invalid ID information")
    @Severity(SeverityLevel.NORMAL)
    public void createRegistrationWithIDStatusCompleted() {
        applicationID = InstitutionEndpoints.generateApplicationIDForCompletedStatus();
        registrationIDComplete = RequestBodyForRegistrationIDComplete.generateRequestBody();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"));
    }

}


