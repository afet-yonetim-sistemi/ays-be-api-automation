package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.datasource.AdminRegistrationApplicationDataSource;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class AdminRegistrationApplicationRejectTest {
    @Test(groups = {"Regression", "SuperAdmin", "Smoke"})
    public void rejectApplicationPositive() {

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

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload
                .generate();
        Response completeResponse = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        completeResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Response response = AdminRegistrationApplicationEndpoints.findById(id);
        response.then()
                .body("completeResponse.status", equalTo(AdminRegistrationApplicationStatus.REJECTED.toString()));
    }

    @Test(groups = {"Regression", "SuperAdmin", "Smoke"}, enabled = false)
    public void rejectARejectedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload
                .generate();

        AdminRegistrationApplicationEndpoints.complete(id, completePayload);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload
                .generate();
        AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);

        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectAnApprovedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationCompletePayload completePayload = AdminRegistrationApplicationCompletePayload.generate();
        AdminRegistrationApplicationEndpoints.complete(id, completePayload);

        AdminRegistrationApplicationEndpoints.approve(id);

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, enabled = false)
    public void rejectANotCompletedApplication() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidRejectReason", dataProviderClass = AysDataProvider.class)
    public void rejectAnApplicationWithInvalidReason(String invalidRejectReason) {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationRejectPayload rejectPayload = AdminRegistrationApplicationRejectPayload.generate();
        rejectPayload.setRejectReason(invalidRejectReason);
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("size must be between 40 and 512"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo(invalidRejectReason));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void rejectAnApplicationWithInvalidApplicationId() {

        String id = "invalidApplicationID";

        AdminRegistrationApplicationRejectPayload rejectPayload = new AdminRegistrationApplicationRejectPayload();

        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be a valid UUID"))
                .body("subErrors[0].field", equalTo("id"))
                .body("subErrors[0].type", equalTo("String"))
                .body("subErrors[0].value", equalTo("invalidApplicationID"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void rejectAnApplicationWithMissingReasonField() {

        String institutionId = AysConfigurationProperty.TestVolunteerFoundation.ID;
        String reason = AysRandomUtil.generateReasonString();
        AdminRegistrationApplicationCreatePayload createPayload = AdminRegistrationApplicationCreatePayload
                .generate(institutionId, reason);
        AdminRegistrationApplicationEndpoints.create(createPayload);

        String id = AdminRegistrationApplicationDataSource.findLastCreatedId();

        AdminRegistrationApplicationRejectPayload rejectPayload = new AdminRegistrationApplicationRejectPayload();
        Response response = AdminRegistrationApplicationEndpoints.reject(id, rejectPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("rejectReason"))
                .body("subErrors[0].type", equalTo("String"));
    }
}
