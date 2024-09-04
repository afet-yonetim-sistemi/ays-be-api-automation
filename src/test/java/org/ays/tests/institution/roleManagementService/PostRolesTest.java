package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.endpoints.Authorization;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Orders;
import org.ays.payload.Pageable;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.RolesListFilter;
import org.ays.payload.RolesListPayload;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostRolesTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void rolesListForAdminOne() {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        int totalElementCount = DatabaseUtility.verifyRoleCountForFoundation("Volunteer Foundation");

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetAdminAccessToken());

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No roles under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount))
                .spec(AysResponseSpecs.expectRolesListInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.filteredBy.institutionId", notNullValue());

    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void rolesListForAdminTwo() {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        int totalElementCount = DatabaseUtility.verifyRoleCountForFoundation("Disaster Foundation");

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetAdminTwoAccessToken());

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No roles under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount))
                .spec(AysResponseSpecs.expectRolesListInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.filteredBy.institutionId", notNullValue());

    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin", "Institution"})
    public void rolesListForSuperAdmin() {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        int totalElementCount = DatabaseUtility.verifyRoleCountForFoundation("Afet Yönetim Sistemi");

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetSuperAdminAccessToken());

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No roles under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount))
                .spec(AysResponseSpecs.expectRolesListInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.filteredBy.institutionId", notNullValue());

    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void rolesListWithFilter() {
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        InstitutionEndpoints.createRole(roleCreatePayload);

        String name = roleCreatePayload.getName();

        RolesListPayload rolesListPayload = RolesListPayload.generateWithFilter(roleCreatePayload);

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetTestAdminAccessToken());

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No roles under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectRolesListInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.content[0].name", is(name))
                .body("response.filteredBy.institutionId", notNullValue());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPageableData", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidPageable(int page, int pageSize, ErrorMessage errorMessage, String field, String type) {
        RolesListPayload rolesListPayload = RolesListPayload.generate();
        Pageable pageable = Pageable.generate(page, pageSize);
        rolesListPayload.setPageable(pageable);

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));


    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidOrderData", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidOrders(String property, String direction, ErrorMessage errorMessage, String field, String type) {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        List<Orders> orders = Orders.generate(property, direction);
        rolesListPayload.getPageable().setOrders(orders);

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidRoleName(String name, ErrorMessage errorMessage, String field, String type) {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        RolesListFilter rolesListFilter = new RolesListFilter();
        rolesListFilter.setName(name);

        rolesListPayload.setFilter(rolesListFilter);

        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForRoleList", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidStatuses(List<String> statuses, ErrorMessage errorMessage, String field, String type) {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        RolesListFilter rolesListFilter = new RolesListFilter();
        rolesListFilter.setStatuses(statuses);

        rolesListPayload.setFilter(rolesListFilter);


        Response response = InstitutionEndpoints.listRoles(rolesListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

}