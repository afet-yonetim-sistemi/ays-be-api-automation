package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetAdminTokenTest {
    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getTokenForValidAdmin() {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = DataProvider.class)
    public void getTokenWithInvalidEmailAddress(String emailAddress, String errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        adminCredentials.setEmailAddress(emailAddress);
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithInvalidPassword() {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        adminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }


}
