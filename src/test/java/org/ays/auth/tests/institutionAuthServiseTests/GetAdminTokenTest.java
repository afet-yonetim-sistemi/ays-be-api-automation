package org.ays.auth.tests.institutionAuthServiseTests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetAdminTokenTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void getTokenForValidAdmin() {
        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = AysDataProvider.class)
    public void getTokenWithInvalidEmailAddress(String emailAddress, String errorMessage, String field, String type) {
        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        loginPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithUnAuthUserEmailAddress() {
        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        loginPayload.setEmailAddress("email@gmail.com");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithInvalidPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        loginPayload.setPassword("123456");
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"})
    public void getTokenWithNullPassword() {
        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        loginPayload.setPassword(null);
        Response response = AuthEndpoints.token(loginPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

}
