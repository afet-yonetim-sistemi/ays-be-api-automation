package org.ays.tests.auth.landingAuthServiceTests;

import io.restassured.response.Response;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.payload.SourcePage;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserTokenTest {

    @Test(groups = {"Smoke", "Regression", "User"})
    public void getTokenForValidUser() {
        UserCredentials userCredentials = UserCredentials.generate();
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectGetTokenResponseSpec());
    }

    @Test(groups = {"Regression", "User"}, dataProvider = "invalidEmailAddressForGetAdminToken", dataProviderClass = DataProvider.class)
    public void getUserTokenWithInvalidUsername(String emailAddress, String errorMessage, String field, String type) {
        UserCredentials userCredentials = UserCredentials.generate();
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
        UserCredentials userCredentials = UserCredentials.generate();
        userCredentials.setEmailAddress("email@gmail.com");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithInvalidPassword() {
        UserCredentials userCredentials = UserCredentials.generate();
        userCredentials.setPassword("wrongPassword");
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithNullPassword() {
        UserCredentials userCredentials = UserCredentials.generate();
        userCredentials.setPassword(null);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectNullCredentialFieldErrorSpec("password"));
    }

    @Test(groups = {"Regression", "User"})
    public void getUserTokenWithUnAuthSourcePage() {
        UserCredentials userCredentials = UserCredentials.generate();
        userCredentials.setSourcePage(SourcePage.INSTITUTION);
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        response.then()
                .spec(AysResponseSpecs.expectUnauthorizedResponseSpec());
    }

}
