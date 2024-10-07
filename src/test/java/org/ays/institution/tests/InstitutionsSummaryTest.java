package org.ays.institution.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysLogUtil;
import org.ays.common.util.AysResponseSpecs;
import org.ays.institution.endpoints.InstitutionEndpoints;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

public class InstitutionsSummaryTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void listInstitutionsPositive() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = InstitutionEndpoints.findAllSummary(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        if (response.jsonPath().getList("response").isEmpty()) {
            AysLogUtil.info("There are no institutions");
        } else {
            response.then()
                    .body("response*.id", everyItem(notNullValue()))
                    .body("response*.name", everyItem(notNullValue()));
        }
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}
