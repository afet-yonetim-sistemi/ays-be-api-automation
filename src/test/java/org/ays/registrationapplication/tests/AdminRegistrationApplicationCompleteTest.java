package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationRejectPayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;


public class AdminRegistrationApplicationCompleteTest {

    @Test(groups = {"Smoke", "Regression"})
    public void completeApplicationRegistrationPositive() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidFirstName(String invalidFirstName, AysErrorMessage errorMessage) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setFirstName(invalidFirstName);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo("firstName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidFirstAndLastNamesDataForAdminRegistration", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidLastName(String invalidFLastName, AysErrorMessage errorMessage) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setLastName(invalidFLastName);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo("lastName"))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression"}, enabled = false, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidEmail(String invalidEmail, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        completePayload.setEmailAddress(invalidEmail);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPhoneNumberDataForRegistrationComplete", dataProviderClass = AysDataProvider.class)
    public void completeApplicationRegistrationWitInvalidPhoneNumber(String countryCode, String lineNumber, String errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        completePayload.setPhoneNumber(phoneNumber);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression"})
    public void completeApplicationRegistrationWithInvalidFormatID() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String applicationID = "invalidID";
        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(applicationID, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());

    }

    @Test(groups = {"Regression"})
    public void completeApplicationRegistrationWithIDCompletedStatus() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestDisasterFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();

        AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void completeRegistrationApplicationWithExistEmail() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);

        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String latestId = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload latestCompletePayload = AdminRegistrationApplicationCompletePayload.generate();
        latestCompletePayload.setEmailAddress(completePayload.getEmailAddress());
        Response response = AdminRegistrationApplicationEndpoints.complete(latestId, completePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("user already exist! emailAddress:" + completePayload.getEmailAddress()));
    }

    @Test(groups = {"Regression"})
    public void completeRegistrationApplicationWithExistPhoneNumber() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);

        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String latestId = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload latestCompletePayload = AdminRegistrationApplicationCompletePayload.generate();
        latestCompletePayload.setPhoneNumber(completePayload.getPhoneNumber());
        Response response = AdminRegistrationApplicationEndpoints.complete(latestId, latestCompletePayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("user already exist! countryCode:" + completePayload.getPhoneNumber().getCountryCode() + " , lineNumber:" + completePayload.getPhoneNumber().getLineNumber()));
    }

    @Test(groups = "Regression")
    public void completeRegistrationApplicationUsingRejectedApplicationId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload
                .generate();
        AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);

        AdminRegistrationApplicationCompletePayload completePayloadForRejectApplication = AdminRegistrationApplicationCompletePayload
                .generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayloadForRejectApplication, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());

    }

    @Test(groups = "Regression")
    public void completeRegistrationApplicationUsingApprovedApplicationId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload, accessToken);

        AdminRegistrationApplicationEndpoints.approve(id, accessToken);

        AdminRegistrationApplicationCompletePayload completePayloadForApprovedApplication = AdminRegistrationApplicationCompletePayload
                .generate();

        Response response = AdminRegistrationApplicationEndpoints.complete(id, completePayloadForApprovedApplication, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());

    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}