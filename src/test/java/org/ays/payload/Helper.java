package org.ays.payload;


import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.InstitutionEndpoints;


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

    public static String getAdminRefreshToken(AdminCredentials adminCredentials) {
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        return response.jsonPath().getString("response.refreshToken");
    }

}
