package org.ays.tests.institution.adminregistrationmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.RegistrationApplicationCompletePayload;
import org.ays.payload.RejectReason;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostAdminRegistrationApplicationApproveTest {
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void approveApplicationPositive() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response getStatus = InstitutionEndpoints.getRegistrationApplicationId(applicationId);
        getStatus.then()
                .body("response.status", equalTo("VERIFIED"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveAnAlreadyApprovedApplication() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        InstitutionEndpoints.postRegistrationApplicationApprove(applicationId);
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveARejectedApplication() {
        String applicationId = InstitutionEndpoints.generateApplicationID();
        InstitutionEndpoints.postRegistrationApplicationIDComplete(applicationId, RegistrationApplicationCompletePayload.generate());
        InstitutionEndpoints.postRegistrationApplicationReject(applicationId, RejectReason.generate());
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove(applicationId);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void approveANotCompletedApplication() {
        String applicationID = InstitutionEndpoints.generateApplicationID();
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove(applicationID);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void approveApplicationWithInvalidId() {
        Response response = InstitutionEndpoints.postRegistrationApplicationApprove("invalidApplicationID");
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

}
