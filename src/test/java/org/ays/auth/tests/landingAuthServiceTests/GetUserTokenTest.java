package org.ays.auth.tests.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.model.enums.SourcePage;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserTokenTest {

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getTokenForValidUser() {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = AysDataProvider.class)
    public void getUserTokenWithInvalidUsername(String emailAddress, String errorMessage, String field, String type) {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        loginPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithUnAuthUserEmailAddress() {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        loginPayload.setEmailAddress("email@gmail.com");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithInvalidPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        loginPayload.setPassword("wrongPassword");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithNullPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        loginPayload.setPassword(null);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("password"));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithUnAuthSourcePage() {
        LoginPayload loginPayload = LoginPayload.generateAsUserOne();
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
