package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostAdminRegistrationApplicationApproveTest {
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void approveApplicationPositive() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getStatus = AdminRegistrationApplicationEndpoints.getRegistrationApplicationId(applicationId);
        getStatus.then()
                .body("response.status", equalTo("VERIFIED"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveAnAlreadyApprovedApplication() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationId);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveARejectedApplication() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveANotCompletedApplication() {
        String applicationID = AdminRegistrationApplicationEndpoints.generateApplicationID();
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void approveApplicationWithInvalidId() {
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove("invalidApplicationID");
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

}
