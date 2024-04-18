package org.ays.tests.institution.adminregistrationmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.ApplicationStatus;
import org.ays.payload.RegistrationApplicationCompletePayload;
import org.ays.payload.RejectReason;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostAdminRegistrationApplicationRejectTest {
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void rejectApplicationPositive() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
        Response getStatus = InstitutionEndpoints.getRegistrationApplicationId(applicationId);
        getStatus.then()
                .body("response.status", equalTo(ApplicationStatus.REJECTED.toString()));
    }

    @Test(groups = {"Regression", "SuperAdmin", "Smoke"}, enabled = false)
    public void rejectARejectedApplication() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectAnApprovedApplication() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        InstitutionEndpoints.postRegistrationApplicationApprove(applicationId);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectANotCompletedApplication() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidRejectReason", dataProviderClass = DataProvider.class)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason) {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        RejectReason reason = new RejectReason();
        reason.setRejectReason(invalidRejectReason);
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, reason);
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
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void rejectAnApplicationWithMissingReasonField() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        RejectReason reason = new RejectReason();
        Response response = InstitutionEndpoints.postRegistrationApplicationReject(applicationId, reason);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"));
    }
}
