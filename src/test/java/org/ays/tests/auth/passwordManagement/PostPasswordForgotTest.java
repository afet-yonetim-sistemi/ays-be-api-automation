package org.ays.tests.auth.passwordManagement;

import io.restassured.response.Response;
import org.ays.auth.payload.PasswordForgotPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.Test;

public class PostPasswordForgotTest {
    @Test(groups = {"Smoke", "Regression"})
    public void postPasswordForgotPositive() {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        String emailAddress = DatabaseUtility.fetchFirstUserEmailAddress();
        passwordForgotPayload.setEmailAddress(emailAddress);

        Response response = InstitutionAuthEndpoints.postPasswordForgot(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddress", dataProviderClass = DataProvider.class)
    public void postPasswordForgotNegative(String emailAddress, AysErrorMessage errorMessage, String field, String type) {
        PasswordForgotPayload passwordForgotPayload = new PasswordForgotPayload();
        passwordForgotPayload.setEmailAddress(emailAddress);
        Response response = InstitutionAuthEndpoints.postPasswordForgot(passwordForgotPayload);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

}
