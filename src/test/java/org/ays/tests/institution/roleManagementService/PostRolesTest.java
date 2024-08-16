package org.ays.tests.institution.roleManagementService;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.RolesListPayload;
import org.ays.tests.database.aysInstitutionName.AfetYonetimSistemi;
import org.ays.tests.database.aysInstitutionName.DisasterFoundation;
import org.ays.tests.database.aysInstitutionName.VolunteerFoundation;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostRolesTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void rolesListForAdminOne() {
        RolesListPayload rolesListPayload = RolesListPayload.generate();

        VolunteerFoundation volunteerFoundation = new VolunteerFoundation();
        VolunteerFoundation.setUp();
        volunteerFoundation.testVolunteerFoundationRoleCount();
        int totalElementCount = volunteerFoundation.getDbUserCount();
        VolunteerFoundation.tearDown();

        Response response = InstitutionEndpoints.listRoles(rolesListPayload);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
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

        DisasterFoundation disasterFoundation = new DisasterFoundation();
        DisasterFoundation.setUp();
        disasterFoundation.testDisasterFoundationRoleCount();
        int totalElementCount = disasterFoundation.getDbUserCount();
        DisasterFoundation.tearDown();

        Response response = InstitutionEndpoints.listRolesForAdminTwo(rolesListPayload);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
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

        AfetYonetimSistemi afetYonetimSistemi = new AfetYonetimSistemi();
        AfetYonetimSistemi.setUp();
        afetYonetimSistemi.testAYSRoleCount();
        int totalElementCount = afetYonetimSistemi.getDbUserCount();
        AfetYonetimSistemi.tearDown();

        Response response = InstitutionEndpoints.listRolesForSuperAdmin(rolesListPayload);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
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

        Response response = InstitutionEndpoints.listRolesForTestAdmin(rolesListPayload);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectRolesListInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.content[0].name", is(name))
                .body("response.filteredBy.institutionId", notNullValue());

    }

}
