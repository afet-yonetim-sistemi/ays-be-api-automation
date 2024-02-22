package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Helper;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetAdminTokenTest {
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
}
