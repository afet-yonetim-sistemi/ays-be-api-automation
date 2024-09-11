package org.ays.tests.auth.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.auth.model.enums.SourcePage;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.LoginPayload;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetSuperAdminTokenTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getTokenForValidSuperAdmin() {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = DataProvider.class)
    public void getTokenWithInvalidEmailAddress(String emailAddress, String errorMessage, String field, String type) {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        superAdminCredentials.setEmailAddress(emailAddress);
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithUnAuthUserEmailAddress() {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        superAdminCredentials.setEmailAddress("email@gmail.com");
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithInvalidPassword() {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        superAdminCredentials.setPassword("1234");
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithNullPassword() {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        superAdminCredentials.setPassword(null);
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithUnAuthSourcePage() {
        LoginPayload superAdminCredentials = LoginPayload.generate();
        superAdminCredentials.setSourcePage(SourcePage.LANDING);
        Response response = InstitutionAuthEndpoints.getSuperAdminToken(superAdminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }
}
