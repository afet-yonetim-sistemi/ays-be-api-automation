package org.ays.payload;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.UserAuthEndpoints;

@Getter
@Setter
public class Token {

    private String accessToken;
    private String refreshToken;
    private String accessTokenExpiresAt;

    public static Token generateUserToken(LoginPayload userCredentials) {
        Token token = new Token();
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
        return token;
    }

    public static Token generateAdminToken(LoginPayload adminCredentials) {
        Token token = new Token();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
        return token;
    }

    public static Token generateSuperAdminToken(LoginPayload superAdminCredentials) {
        Token token = new Token();
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
        return token;
    }

}
