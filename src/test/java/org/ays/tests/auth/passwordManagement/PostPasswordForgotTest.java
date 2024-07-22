package org.ays.tests.auth.passwordManagement;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.payload.PasswordForgotPayload;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

public class PostPasswordForgotTest {
    @Test(groups = {"Smoke", "Regression"})
    public void postPasswordForgotPositive() {
        String emailAddress = DatabaseUtility.fetchFirstUserEmailAddress();
        Response response = InstitutionAuthEndpoints.postPasswordForgot(emailAddress);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = DataProvider.class)
    public void postPasswordForgotNegative(String emailAddress, ErrorMessage errorMessage, String field, String type) {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        passwordForgotPayload.setEmailAddress(emailAddress);
        Response response = InstitutionAuthEndpoints.postPasswordForgot(emailAddress);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

}
