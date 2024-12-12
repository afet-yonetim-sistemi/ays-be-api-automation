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

public class AdminRegistrationApplicationRejectTest {
    @Test(groups = {"Smoke", "Regression"})
    public void rejectApplicationPositive() {

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

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload
                .generate();
        Response completeResponse = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);
        completeResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response response = AdminRegistrationApplicationEndpoints.findById(id, accessToken);
        response.then()
                .body("response.status", equalTo(AdminRegistrationApplicationStatus.REJECTED.name()));
    }

    @Test(groups = {"Smoke", "Regression"})
    public void rejectARejectedApplication() {

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

        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_ALREADY_REJECTED.getMessage()));
    }

    @Test(groups = {"Regression"})
    public void rejectAnApprovedApplication() {

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

        AdminRegistrationApplicationEndpoints.approve(id, accessToken);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_ALREADY_APPROVED.getMessage()));
    }


    @Test(groups = {"Regression"})
    public void rejectANotCompletedApplication() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message",
                        containsString(
                                AysErrorMessage.ADMIN_REGISTRATION_APPLICATION_IS_NOT_COMPLETED.getMessage()));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidRejectReasonDataForAdminRegistrationApplicationReject",
            dataProviderClass = AysDataProvider.class)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload, accessToken);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        rejectPayload.setRejectReason(invalidRejectReason);
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidIdFormat", dataProviderClass = AysDataProvider.class)
    public void rejectAnApplicationWithInvalidApplicationId(String invalidApplicationId, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.reject(invalidApplicationId, rejectPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
