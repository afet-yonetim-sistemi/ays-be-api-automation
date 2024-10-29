package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

public class PasswordForgotTest {

    @Test(groups = {"Smoke", "Regression"})
    public void forgotPasswordPositiveTest() {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String emailAddress = UserDataSource.findAnyEmailAddress();
        passwordForgotPayload.setEmailAddress(emailAddress);

        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = AysDataProvider.class)
    public void forgotPasswordNegativeTest(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        passwordForgotPayload.setEmailAddress(emailAddress);
        Response response = AuthEndpoints.forgotPassword(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

}
