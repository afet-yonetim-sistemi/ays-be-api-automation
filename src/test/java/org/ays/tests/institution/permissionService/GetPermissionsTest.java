package org.ays.tests.institution.permissionService;

import io.restassured.response.Response;
import org.ays.auth.datasource.PermissionDataSource;
import org.ays.auth.endpoints.PermissionEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class GetPermissionsTest {

    @Test(groups = {"Smoke", "Regression"})
    public void nonSuperUsersShouldNotSeeSuperPermissions() {

        Response response = PermissionEndpoints.getAdminsPermissions();
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

        Response response = PermissionEndpoints.getAdminsPermissions();
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> listOfNameValuesFromResponse = response.jsonPath().getList("response.name");
        List<String> listOfNameValuesFromDB = PermissionDataSource.findAllPermissionNamesByIsSuper(false);

        for (String dbName : listOfNameValuesFromDB) {
            Assert.assertTrue(listOfNameValuesFromResponse.contains(dbName), "Name from database found in response: " + dbName);
        }
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void superUsersShouldSeeAllPermissions() {

        Response response = PermissionEndpoints.getSuperAdminsPermissions();
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
}
