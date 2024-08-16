package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminCredentials;
import org.ays.payload.SourcePage;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysConfigurationProperty;

@UtilityClass
public class Authorization {

    public static String loginAndGetSuperAdminAccessToken() {
        AdminCredentials superAdminCredentials = new AdminCredentials();
        superAdminCredentials.setEmailAddress(AysConfigurationProperty.SuperAdminUserOne.EMAIL_ADDRESS);
        superAdminCredentials.setPassword(AysConfigurationProperty.SuperAdminUserOne.PASSWORD);
        superAdminCredentials.setSourcePage(SourcePage.INSTITUTION);

        Response response = InstitutionAuthEndpoints.getAdminToken(superAdminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetAdminAccessToken() {

        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserOne.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetAdminTwoAccessToken() {

        AdminCredentials adminCredentials = AdminCredentials.generateForAdminTwo();

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetTestAdminAccessToken() {

        AdminCredentials adminCredentials = AdminCredentials.generateForTestAdmin();

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetUserAccessToken(String username, String password) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmailAddress(username);
        userCredentials.setPassword(password);

        Response response = UserAuthEndpoints.getUserToken(userCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }


}
