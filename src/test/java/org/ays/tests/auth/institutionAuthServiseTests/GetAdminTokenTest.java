package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Helper;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

public class GetAdminTokenTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getTokenForValidAdmin() {
        AdminCredentials adminCredentials = Helper.setIntsAdminCredentials();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getAdminTokenWithInvalidUsername() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setUsername("invalidUsername");
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
