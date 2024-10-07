package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
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
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void approveApplicationPositive() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload);

        Response response = AdminRegistrationApplicationEndpoints.approve(id);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getStatus = AdminRegistrationApplicationEndpoints.findById(id);
        getStatus.then()
                .body("response.status", equalTo(AdminRegistrationApplicationStatus.APPROVED.name()));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveAnAlreadyApprovedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints
                .complete(id, completePayload);

        AdminRegistrationApplicationEndpoints.approve(id);

        Response response = AdminRegistrationApplicationEndpoints.approve(id);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveARejectedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();
        AdminRegistrationApplicationEndpoints
                .complete(id, completePayload);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);

        Response response = AdminRegistrationApplicationEndpoints.approve(id);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveANotCompletedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        Response response = AdminRegistrationApplicationEndpoints.approve(id);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void approveApplicationWithInvalidId() {
        Response response = AdminRegistrationApplicationEndpoints.approve("invalidApplicationID");
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

}
