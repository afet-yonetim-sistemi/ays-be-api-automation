package org.ays.tests.institution.adminmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.AdminsListPayload;
import org.ays.payload.Pageable;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class PostAdminsTest {
    Pageable pageable;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        pageable = new Pageable();
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "positivePaginationData", dataProviderClass = org.ays.utility.DataProvider.class)
    public void listAdminsWithValidPageAndPageSize(int page, int pageSize) {
        pageable.setPage(page);
        pageable.setPageSize(pageSize);
        Response response = InstitutionEndpoints.listAdmins(AdminsListPayload.generate(pageable));
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails());
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No admins found");
        } else {
            response.then()
                    .body("response.content", instanceOf(List.class))
                    .body("response.content[0].id", notNullValue())
                    .body("response.content[0].username", notNullValue())
                    .body("response.content[0].firstName", notNullValue())
                    .body("response.content[0].lastName", notNullValue())
                    .body("response.content[0].status", notNullValue())
                    .body("response.content[0].institution.createdUser", notNullValue())
                    .body("response.content[0].institution.createdAt", notNullValue())
                    .body("response.content[0].institution", hasKey("updatedUser"))
                    .body("response.content[0].institution", hasKey("updatedAt"))
                    .body("response.content[0].institution.id", notNullValue())
                    .body("response.content[0].institution.name", notNullValue());
        }
    }

    @Test(groups = {"Regression", "Institution"})
    public void listAdminsWithNullPageValue() {
        pageable.setPageSize(10);
        Response response = InstitutionEndpoints.listAdmins(AdminsListPayload.generate(pageable));
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("page"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void listAdminsWithNullPageSizeValue() {
        pageable.setPage(1);
        Response response = InstitutionEndpoints.listAdmins(AdminsListPayload.generate(pageable));
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test(groups = {"Regression", "Institution"})
    public void listAdminsWithNullPageAndPageSizeFields() {
        Response response = InstitutionEndpoints.listAdmins(AdminsListPayload.generate(pageable));
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"))
                .body("subErrors[1].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[1].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[1].type", equalTo("int"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePageableData", dataProviderClass = org.ays.utility.DataProvider.class)
    public void listAdminsWithNegativeScenarios(int page, int pageSize) {
        pageable.setPage(page);
        pageable.setPageSize(pageSize);
        Response response = InstitutionEndpoints.listAdmins(AdminsListPayload.generate(pageable));
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));
    }
}
