package org.ays.tests.auth;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Helper;
import org.ays.payload.RefreshToken;
import org.ays.payload.Token;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.ays.utility.AysResponseSpecs;

public class InstitutionAuthServiceTest {
    AdminCredentials adminCredentials;

    @BeforeMethod
    public void setup() {
        adminCredentials = new AdminCredentials();
    }


    @Test
    public void getTokenForValidAdmin() {
        adminCredentials = Helper.setIntsAdminCredentials();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void getAdminTokenWithInvalidUsername() {
        adminCredentials.setUsername("invalidUsername");
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }

    @Test
    public void adminTokenRefresh() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(Helper.getAdminRefreshToken(Helper.setIntsAdminCredentials()));
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void adminInvalidateToken() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        Response response = InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec());
    }

    @Test
    public void testAdminInvalidRefreshTokenForAccessTokenCreation() {
        Token token = Helper.getAdminToken(Helper.setIntsAdminCredentials());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token.getRefreshToken());
        InstitutionAuthEndpoints.adminInvalidateToken(token.getAccessToken(), refreshToken);
        Response response = InstitutionAuthEndpoints.adminTokenRefresh(refreshToken);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }
}
