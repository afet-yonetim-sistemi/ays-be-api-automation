package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Helper;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetAdminTokenTest {
    @Test
    public void getTokenForValidAdmin() {
        AdminCredentials adminCredentials;
        adminCredentials = Helper.setIntsAdminCredentials();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.successResponseSpec())
                .spec(AysResponseSpecs.getTokenResponseSpec());
    }

    @Test
    public void getAdminTokenWithInvalidUsername() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setUsername("invalidUsername");
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.unauthorizedResponseSpec());
    }
}