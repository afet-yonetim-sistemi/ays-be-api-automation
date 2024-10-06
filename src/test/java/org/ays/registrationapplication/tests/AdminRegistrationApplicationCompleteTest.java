package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class AdminRegistrationApplicationCompleteTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void completeApplicationRegistrationPositive() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidFirstName(String invalidFirstName, String errorMessage) {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setFirstName(invalidFirstName);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidLastName(String invalidFLastName, String errorMessage) {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setLastName(invalidFLastName);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidEmailForAdminRegistration", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidEmail(String invalidEmail, String errorMessage) {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setEmailAddress(invalidEmail);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo("emailAddress"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidPhoneNumberDataForRegistrationComplete", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidPhoneNumber(String countryCode, String lineNumber, String errorMessage, String field, String type) {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        completePayload.setPhoneNumber(phoneNumber);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload);
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

        Response response = AdminRegistrationApplicationEndpoints.complete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());

    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeApplicationRegistrationWithIDCompletedStatus() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationIDForCompletedStatus();
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(applicationID, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("ADMIN USER REGISTER APPLICATION NOT EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeRegistrationApplicationWithExistEmail() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload);

        AdminRegistrationApplicationEndpoints.create(createPayload);

        String latestId = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload latestCompletePayload = AdminRegistrationApplicationCompletePayload.generate();
        latestCompletePayload.setEmailAddress(completePayload.getEmailAddress());
        Response response = AdminRegistrationApplicationEndpoints.complete(latestId, completePayload);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("ADMIN USER ALREADY EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void completeRegistrationApplicationWithExistPhoneNumber() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload);

        AdminRegistrationApplicationEndpoints.create(createPayload);

        String latestId = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload latestCompletePayload = AdminRegistrationApplicationCompletePayload.generate();
        latestCompletePayload.setPhoneNumber(completePayload.getPhoneNumber());
        Response response = AdminRegistrationApplicationEndpoints.complete(latestId, latestCompletePayload);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("ADMIN USER ALREADY EXIST!"));
    }

}