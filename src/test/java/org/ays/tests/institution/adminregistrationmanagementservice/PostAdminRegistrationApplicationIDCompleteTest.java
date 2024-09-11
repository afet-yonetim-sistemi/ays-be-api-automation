package org.ays.tests.institution.adminregistrationmanagementservice;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.payload.AysPhoneNumber;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class PostAdminRegistrationApplicationIDCompleteTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void completeApplicationRegistrationPositive() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidFirstName(String invalidFirstName, String errorMessage) {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setFirstName(invalidFirstName);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidLastName(String invalidFLastName, String errorMessage) {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setLastName(invalidFLastName);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidEmailForAdminRegistration", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidEmail(String invalidEmail, String errorMessage) {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setEmail(invalidEmail);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("email"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidPhoneNumberDataForRegistrationComplete", dataProviderClass = DataProvider.class)
    public void completeApplicationRegistrationWitInvalidPhoneNumber(String countryCode, String lineNumber, String errorMessage, String field, String type) {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        completePayload.setPhoneNumber(phoneNumber);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeApplicationRegistrationWithInvalidFormatID() {
        String applicationID = "invalidID";
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeApplicationRegistrationWithIDCompletedStatus() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationIDForCompletedStatus();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeRegistrationApplicationWithExistEmail() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);

        String email = completePayload.getEmail();
        String newApplicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload newRegistrationIDComplete = AdminRegistrationApplicationCompletePayload.generate();
        newRegistrationIDComplete.setEmail(email);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(newApplicationID, newRegistrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("ADMIN USER ALREADY EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeRegistrationApplicationWithExistPhoneNumber() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationID, completePayload);

        AysPhoneNumber phoneNumber = completePayload.getPhoneNumber();
        String newApplicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationCompletePayload newRegistrationIDComplete = AdminRegistrationApplicationCompletePayload.generate();
        newRegistrationIDComplete.setPhoneNumber(phoneNumber);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(newApplicationID, newRegistrationIDComplete);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("ADMIN USER ALREADY EXIST!"));
    }

}