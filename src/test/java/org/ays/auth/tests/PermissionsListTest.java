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

        LoginPayload loginPayload = LoginPayload.generateAsAfetYonetimSistemiAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = PermissionEndpoints.listPermissions(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> setOfNameValuesFromResponse = response.jsonPath().getList("response.name");
        List<String> setOfNameValuesFromDB = PermissionDataSource.findAllPermissionNames();

        Assert.assertEquals(
                setOfNameValuesFromResponse,
                setOfNameValuesFromDB,
                "The names from the response and the database do not match."
        );
    }

    @Test(groups = {"Smoke", "Regression"})
    public void nonSuperUsersShouldNotSeeSuperPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        Response response = PermissionEndpoints.listPermissions(accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> listOfNameValuesFromResponse = response.jsonPath().getList("response.name");
        List<String> listOfNameValuesFromDB = PermissionDataSource.findAllPermissionNamesByIsSuper(true);

        for (String dbName : listOfNameValuesFromDB) {
            Assert.assertFalse(listOfNameValuesFromResponse.contains(dbName), "Name from database found in response: " + dbName);
        }
    }

    @Test(groups = {"Regression"})
    public void nonSuperUsersShouldSeeNonSuperPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsVolunteerFoundationAdmin();
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
