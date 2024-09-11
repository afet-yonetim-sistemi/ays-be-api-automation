package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.model.enums.SourcePage;
import org.ays.payload.LoginPayload;
import org.ays.utility.AysConfigurationProperty;

@UtilityClass
public class Authorization {

    public static String loginAndGetSuperAdminAccessToken() {
        LoginPayload superAdminCredentials = new LoginPayload();
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

        LoginPayload adminCredentials = new LoginPayload();
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

        LoginPayload adminCredentials = LoginPayload.generateAsAdminUserTwo();

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

    public static String loginAndGetTestAdminAccessToken() {

        LoginPayload adminCredentials = LoginPayload.generateAsTestAdmin();

        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);

        if (!response.jsonPath().getBoolean("isSuccess")) {
            System.out.println(response.jsonPath().prettify());
        }

        return response.jsonPath().getString("response.accessToken");
    }

}
