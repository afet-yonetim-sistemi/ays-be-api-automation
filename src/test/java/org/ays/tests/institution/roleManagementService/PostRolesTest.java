package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.RoleListPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.model.payload.AysPageable;
import org.ays.endpoints.Authorization;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostRolesTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void rolesListForAdminOne() {
        RoleListPayload roleListPayload = RoleListPayload.generate();

        int totalElementCount = RoleDataSource.verifyRoleCountForFoundation("Volunteer Foundation");

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetAdminAccessToken());

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
        RoleListPayload roleListPayload = RoleListPayload.generate();

        int totalElementCount = RoleDataSource.verifyRoleCountForFoundation("Disaster Foundation");

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetAdminTwoAccessToken());

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
        RoleListPayload roleListPayload = RoleListPayload.generate();

        int totalElementCount = RoleDataSource.verifyRoleCountForFoundation("Afet Yönetim Sistemi");

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetSuperAdminAccessToken());

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
        RoleEndpoints.createRole(roleCreatePayload);

        String name = roleCreatePayload.getName();

        RoleListPayload roleListPayload = RoleListPayload.generateWithFilter(roleCreatePayload);

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetTestAdminAccessToken());

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
    public void rolesListUsingInvalidPageable(int page, int pageSize, AysErrorMessage errorMessage, String field, String type) {
        RoleListPayload roleListPayload = RoleListPayload.generate();
        AysPageable pageable = AysPageable.generate(page, pageSize);
        roleListPayload.setPageable(pageable);

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));


    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidOrderData", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidOrders(String property, String direction, AysErrorMessage errorMessage, String field, String type) {
        RoleListPayload roleListPayload = RoleListPayload.generate();

        List<AysOrder> orders = AysOrder.generate(property, direction);
        roleListPayload.getPageable().setOrders(orders);

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidRoleName", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidRoleName(String name, AysErrorMessage errorMessage, String field, String type) {
        RoleListPayload roleListPayload = RoleListPayload.generate();

        RoleListPayload.Filter filter = new RoleListPayload.Filter();
        filter.setName(name);

        roleListPayload.setFilter(filter);

        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForRoleList", dataProviderClass = DataProvider.class)
    public void rolesListUsingInvalidStatuses(List<String> statuses, AysErrorMessage errorMessage, String field, String type) {
        RoleListPayload roleListPayload = RoleListPayload.generate();

        RoleListPayload.Filter filter = new RoleListPayload.Filter();
        filter.setStatuses(statuses);

        roleListPayload.setFilter(filter);


        Response response = RoleEndpoints.listRoles(roleListPayload, Authorization.loginAndGetTestAdminAccessToken());
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

}
