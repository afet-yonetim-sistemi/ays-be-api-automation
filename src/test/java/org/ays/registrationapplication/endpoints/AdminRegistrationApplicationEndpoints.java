package org.ays.registrationapplication.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.auth.payload.AdminRegistrationApplicationRejectPayload;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.AysRestAssured;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class AdminRegistrationApplicationEndpoints {

    public static Response findAll(AdminRegistrationApplicationListPayload listPayload) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-applications")
                .body(listPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response findById(String id) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response create(AdminRegistrationApplicationCreatePayload createPayload) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application")
                .body(createPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response approve(String id) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/approve")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response reject(String id, AdminRegistrationApplicationRejectPayload rejectPayload) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/reject")
                .pathParameter(Map.of("id", id))
                .body(rejectPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response findSummaryById(String id) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}/summary")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response complete(String id, AdminRegistrationApplicationCompletePayload completePayload) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/complete")
                .pathParameter(Map.of("id", id))
                .body(completePayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static String generateApplicationID() {
        AdminRegistrationApplicationCreatePayload adminRegistrationApplicationCreatePayload = AdminRegistrationApplicationCreatePayload
                .generate(AysConfigurationProperty.TestVolunteerFoundation.ID, AysRandomUtil.generateReasonString());

        Response response = create(adminRegistrationApplicationCreatePayload);
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
        Response response = findAll(applicationListPayload);
        if (response.getStatusCode() == 200) {
            return response.then().extract().jsonPath().getString("response.content[0].id");
        } else {
            throw new RuntimeException("Application Id creation failed with status code: " + response.getStatusCode());
        }
    }


}