package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class AdminRegistrationApplicationApproveTest {
    @Test(groups = {"Smoke", "Regression"})
    public void approveApplicationPositive() {

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

        Response response = AdminRegistrationApplicationEndpoints.approve(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getStatus = AdminRegistrationApplicationEndpoints.findById(id, accessToken);
        getStatus.then()
                .body("response.status", equalTo(AdminRegistrationApplicationStatus.APPROVED.name()));
    }

    @Test(groups = {"Regression"})
    public void approveAnAlreadyApprovedApplication() {

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
        AdminRegistrationApplicationEndpoints
                .complete(id, completePayload, accessToken);

        AdminRegistrationApplicationEndpoints.approve(id, accessToken);

        Response response = AdminRegistrationApplicationEndpoints.approve(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_ALREADY_APPROVED.getMessage()));
    }

    @Test(groups = {"Regression"})
    public void approveARejectedApplication() {

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
        AdminRegistrationApplicationEndpoints
                .complete(id, completePayload, accessToken);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);

        Response response = AdminRegistrationApplicationEndpoints.approve(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_ALREADY_REJECTED.getMessage()));
    }


    @Test(groups = {"Regression"})
    public void approveANotCompletedApplication() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        Response response = AdminRegistrationApplicationEndpoints.approve(id, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_IS_NOT_COMPLETED.getMessage()));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void approveApplicationWithInvalidId(String invalidApplicationId, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = AdminRegistrationApplicationEndpoints.approve(invalidApplicationId, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
