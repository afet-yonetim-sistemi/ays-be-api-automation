package org.ays.tests.institution.adminregistrationmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RegistrationApplicationCompletePayload;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class PostAdminRegistrationApplicationIDCompleteTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void completeApplicationRegistrationPositive() {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidFirstName(String invalidFirstName, String errorMessage) {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();
        registrationIDComplete.setFirstName(invalidFirstName);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidLastName(String invalidFLastName, String errorMessage) {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();
        registrationIDComplete.setLastName(invalidFLastName);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidEmailForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidEmail(String invalidEmail, String errorMessage) {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();
        registrationIDComplete.setEmail(invalidEmail);

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("email"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidPhoneNumberDataForRegistrationComplete", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidPhoneNumber(String countryCode, String lineNumber, String errorMessage, String field, String type) {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();

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
    public void completeApplicationRegistrationWithInvalidFormatID() {
        String applicationID = "invalidID";
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeApplicationRegistrationWithIDCompletedStatus() {
        String applicationID = InstitutionEndpoints.generateApplicationIDForCompletedStatus();
        RegistrationApplicationCompletePayload registrationIDComplete = RegistrationApplicationCompletePayload.generate();

        Response response = InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationID, registrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"));
    }

}


