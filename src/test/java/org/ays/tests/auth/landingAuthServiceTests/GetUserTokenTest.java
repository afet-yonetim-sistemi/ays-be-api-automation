package org.ays.tests.auth.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.auth.model.enums.SourcePage;
import org.ays.auth.payload.LoginPayload;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserTokenTest {

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getTokenForValidUser() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = DataProvider.class)
    public void getUserTokenWithInvalidUsername(String emailAddress, String errorMessage, String field, String type) {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        userCredentials.setEmailAddress(emailAddress);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithUnAuthUserEmailAddress() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        userCredentials.setEmailAddress("email@gmail.com");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithInvalidPassword() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        userCredentials.setPassword("wrongPassword");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithNullPassword() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        userCredentials.setPassword(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("password"));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithUnAuthSourcePage() {
        LoginPayload userCredentials = LoginPayload.generateAsUserOne();
        userCredentials.setSourcePage(SourcePage.INSTITUTION);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
