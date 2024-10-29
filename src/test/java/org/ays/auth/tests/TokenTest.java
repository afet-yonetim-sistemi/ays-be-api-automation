package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.model.enums.SourcePage;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

public class TokenTest {

    @Test(groups = {"Smoke", "Regression"})
    public void getTokenForValidAdmin() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void getTokenWithInvalidEmailAddress(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        loginPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void getTokenWithUnAuthUserEmailAddress() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        loginPayload.setEmailAddress("email@gmail.com");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void getTokenWithInvalidPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        loginPayload.setPassword("123456");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void getTokenWithNullPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        loginPayload.setPassword(null);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void getTokenForValidUser() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void getUserTokenWithInvalidUsername(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        loginPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void getUserTokenWithUnAuthUserEmailAddress() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        loginPayload.setEmailAddress("email@gmail.com");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void getUserTokenWithInvalidPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        loginPayload.setPassword("wrongPassword");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression"})
    public void getUserTokenWithNullPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        loginPayload.setPassword(null);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("password"));
    }

    @Test(groups = {"Regression"})
    public void getUserTokenWithUnAuthSourcePage() {
        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationUser();
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
