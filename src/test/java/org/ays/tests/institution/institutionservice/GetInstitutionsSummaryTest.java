package org.ays.tests.institution.institutionservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Helper;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

public class GetInstitutionsSummaryTest {
    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void listInstitutionsPositive() {
        Response response = InstitutionEndpoints.getInstitutionsSummary();
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        if (Helper.extractResponseAsList(response).isEmpty()) {
            AysLogUtil.info("There are no institutions");
        } else {
            response.then()
                    .body("response*.id", everyItem(notNullValue()))
                    .body("response*.name", everyItem(notNullValue()));
        }
    }


}
