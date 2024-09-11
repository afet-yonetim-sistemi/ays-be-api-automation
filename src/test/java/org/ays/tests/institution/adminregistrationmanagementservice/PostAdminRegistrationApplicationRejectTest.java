package org.ays.tests.institution.adminregistrationmanagementservice;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.emergencyapplication.model.enums.AdminUserRegistrationApplicationStatus;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostAdminRegistrationApplicationRejectTest {
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void rejectApplicationPositive() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
        Response getStatus = AdminRegistrationApplicationEndpoints.getRegistrationApplicationId(applicationId);
        getStatus.then()
                .body("response.status", equalTo(AdminUserRegistrationApplicationStatus.REJECTED.toString()));
    }

    @Test(groups = {"Regression", "SuperAdmin", "Smoke"}, enabled = false)
    public void rejectARejectedApplication() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectAnApprovedApplication() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationIDComplete(applicationId, AdminRegistrationApplicationCompletePayload.generate());
        AdminRegistrationApplicationEndpoints.postRegistrationApplicationApprove(applicationId);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectANotCompletedApplication() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidRejectReason", dataProviderClass = DataProvider.class)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason) {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationRejectPayload reason = new AdminRegistrationApplicationRejectPayload();
        reason.setRejectReason(invalidRejectReason);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, reason);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("size must be between 40 and 512"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo(invalidRejectReason));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void rejectAnApplicationWithInvalidApplicationId() {
        String applicationId = "invalidApplicationID";
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, AdminRegistrationApplicationRejectPayload.generate());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void rejectAnApplicationWithMissingReasonField() {
        String applicationId = AdminRegistrationApplicationEndpoints.generateApplicationID();
        AdminRegistrationApplicationRejectPayload reason = new AdminRegistrationApplicationRejectPayload();
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplicationReject(applicationId, reason);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"));
    }
}
