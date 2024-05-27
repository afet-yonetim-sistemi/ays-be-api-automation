package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminCredentials;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysConfigurationProperty;

@UtilityClass
public class Authorization {

    public static String loginAndGetSuperAdminAccessToken() {
        AdminCredentials superAdminCredentials = new AdminCredentials();
        superAdminCredentials.setEmailAddress(AysConfigurationProperty.SuperAdminUserOne.EMAIL);
        superAdminCredentials.setPassword(AysConfigurationProperty.SuperAdminUserOne.PASSWORD);

        Response response = InstitutionAuthEndpoints.getAdminToken(superAdminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetAdminAccessToken() {

        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserOne.EMAIL);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetUserAccessToken(String username, String password) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);

        Response response = UserAuthEndpoints.getUserToken(userCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

}
