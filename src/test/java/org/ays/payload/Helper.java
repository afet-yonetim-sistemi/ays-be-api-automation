package org.ays.payload;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.endpoints.UserEndpoints;

import java.util.List;
import java.util.Map;


@UtilityClass
@Slf4j
public class Helper {


    public static String extractAssignmentIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generate(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");

    }

    public static String extractUserIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listUsers(RequestBodyUsers.generate(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");
    }

    public static void setSupportStatus(String status, String username, String password) {
        String payload = createPayloadWithSupportStatus(status);
        UserEndpoints.updateSupportStatus(payload, username, password);
    }

    public static String createPayloadWithSupportStatus(String supportStatus) {
        return "{\n" +
                "    \"supportStatus\": \"" + supportStatus + "\"\n" +
                "}";
    }

    public static String getAdminRefreshToken(AdminCredentials adminCredentials) {
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        return response.jsonPath().getString("response.refreshToken");
    }

    public static String getUserRefreshToken(UserCredentials userCredentials) {
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        return response.jsonPath().getString("response.refreshToken");
    }

    public static List<Map<String, String>> extractResponseAsList(Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("response");
    }

}

