package org.ays.registrationapplication.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.endpoints.Authorization;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class AdminRegistrationApplicationEndpoints {

    public static Response postRegistrationApplications(AdminRegistrationApplicationListPayload requestBodyInstitution) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-applications")
                .body(requestBodyInstitution)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationId(String applicationID) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationsIdSummary(String applicationID) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}/summary")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplicationApprove(String applicationID) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/approve")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationAdminApplication(AdminRegistrationApplicationCreatePayload adminRegistrationApplicationCreatePayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application")
                .body(adminRegistrationApplicationCreatePayload)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response postRegistrationApplicationReject(String applicationID, AdminRegistrationApplicationRejectPayload adminRegistrationApplicationRejectPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/reject")
                .pathParameter(Map.of("id", applicationID))
                .body(adminRegistrationApplicationRejectPayload)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static String generateApplicationID() {
        AdminRegistrationApplicationCreatePayload adminRegistrationApplicationCreatePayload = AdminRegistrationApplicationCreatePayload.generate(AysConfigurationProperty.InstitutionOne.ID, AysRandomUtil.generateReasonString());
        Response response = postRegistrationAdminApplication(adminRegistrationApplicationCreatePayload);
        if (response.getStatusCode() == 200) {
            return response.then().extract().jsonPath().getString("response.id");
        } else {
            throw new RuntimeException("Application Id creation failed with status code: " + response.getStatusCode());
        }
    }

    public static String generateApplicationIDForCompletedStatus() {
        AdminRegistrationApplicationListPayload.Filter filter = AdminRegistrationApplicationListPayload.Filter
                .generate(AdminRegistrationApplicationStatus.COMPLETED);
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate(filter);
        Response response = postRegistrationApplications(applicationListPayload);
        if (response.getStatusCode() == 200) {
            return response.then().extract().jsonPath().getString("response.content[0].id");
        } else {
            throw new RuntimeException("Application Id creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Response postRegistrationApplicationIDComplete(String applicationID, AdminRegistrationApplicationCompletePayload requestBodyForRegistrationComplete) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/complete")
                .pathParameter(Map.of("id", applicationID))
                .body(requestBodyForRegistrationComplete)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}