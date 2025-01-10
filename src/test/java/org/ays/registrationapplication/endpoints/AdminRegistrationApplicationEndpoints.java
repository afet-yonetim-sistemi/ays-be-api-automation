package org.ays.registrationapplication.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCompletePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationCreatePayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationRejectPayload;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class AdminRegistrationApplicationEndpoints {

    public static Response findAll(AdminRegistrationApplicationListPayload listPayload, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-applications")
                .body(listPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response findById(String id, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response create(AdminRegistrationApplicationCreatePayload createPayload, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application")
                .body(createPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response approve(String id, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/approve")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response reject(String id, AdminRegistrationApplicationRejectPayload rejectPayload, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/reject")
                .pathParameter(Map.of("id", id))
                .body(rejectPayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response findSummaryById(String id, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin-registration-application/{id}/summary")
                .pathParameter(Map.of("id", id))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response complete(String id, AdminRegistrationApplicationCompletePayload completePayload, String accessToken) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-application/{id}/complete")
                .pathParameter(Map.of("id", id))
                .body(completePayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}