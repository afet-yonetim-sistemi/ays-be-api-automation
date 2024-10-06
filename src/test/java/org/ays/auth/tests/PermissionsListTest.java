package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.PermissionEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.util.AysResponseSpecs;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PermissionsListTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void superUsersShouldSeeAllPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = PermissionEndpoints.listPermissions(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> permissionsFromResponse = response.jsonPath().getList("response.name");
        List<String> permissionsFromDb = PermissionDataSource.findAllPermissionNames();

        for (String permissionFromDb : permissionsFromDb) {
            Assert.assertTrue(
                    permissionsFromResponse.contains(permissionFromDb),
                    "Name from database not found in response: " + permissionFromDb
            );
        }
    }

    @Test(groups = {"Smoke", "Regression"})
    public void nonSuperUsersShouldNotSeeSuperPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = PermissionEndpoints.listPermissions(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> permissionsFromResponse = response.jsonPath().getList("response.name");
        List<String> permissionsFromDb = PermissionDataSource.findAllPermissionNamesByIsSuper(true);

        for (String permissionFromDb : permissionsFromDb) {
            Assert.assertFalse(
                    permissionsFromResponse.contains(permissionFromDb),
                    "Name from database found in response: " + permissionFromDb
            );
        }
    }

    @Test(groups = {"Regression"})
    public void nonSuperUsersShouldSeeNonSuperPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = PermissionEndpoints.listPermissions(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> listOfNameValuesFromResponse = response.jsonPath().getList("response.name");
        List<String> listOfNameValuesFromDB = PermissionDataSource.findAllPermissionNamesByIsSuper(false);

        for (String dbName : listOfNameValuesFromDB) {
            Assert.assertTrue(listOfNameValuesFromResponse.contains(dbName), "Name from database found in response: " + dbName);
        }
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }
}
