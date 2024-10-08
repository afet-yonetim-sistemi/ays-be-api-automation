package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class AdminRegistrationApplicationApproveTest {
    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
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

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
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
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
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
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
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
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void approveApplicationWithInvalidId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = AdminRegistrationApplicationEndpoints.approve("invalidApplicationID", accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
