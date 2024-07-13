package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Pageable;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.SuperAdminCredentials;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.Test;


public class PostUsersTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminOne() {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers, adminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForVolunteer());
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminTwo() {
        AdminCredentials adminCredentials = AdminCredentials.generateForAdminTwo();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsersTwo(requestBodyUsers, adminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForDisaster());
        }
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin", "Institution"})
    public void usersListForSuperAdmin() {
        SuperAdminCredentials superAdminCredentials = SuperAdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsersSuperAdmin(requestBodyUsers, superAdminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForAYS());
        }
    }
}
