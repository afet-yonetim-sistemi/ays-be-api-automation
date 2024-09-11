package org.ays.tests.institution.permissionService;

import io.restassured.response.Response;
import org.ays.auth.permission.endpoints.PermissionEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DatabaseUtility;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.ays.utility.DatabaseUtility.getColumNameFromTableWhereValueIs;

public class GetPermissionsTest {

    @Test(groups = {"Smoke", "Regression"})
    public void nonSuperUsersShouldNotSeeSuperPermissions() {

        Response response = PermissionEndpoints.getAdminsPermissions();
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        List<String> listOfNameValuesFromResponse = response.jsonPath().getList("response.name");
        List<String> listOfNameValuesFromDB = getColumNameFromTableWhereValueIs("name", "AYS_PERMISSION", "IS_SUPER", "1");

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
        List<String> listOfNameValuesFromDB = getColumNameFromTableWhereValueIs("name", "AYS_PERMISSION", "IS_SUPER", "0");

        for (String dbName : listOfNameValuesFromDB) {
            Assert.assertTrue(listOfNameValuesFromResponse.contains(dbName), "Name from database found in response: " + dbName);
        }
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void superUsersShouldSeeAllPermissions() {

        Response response = PermissionEndpoints.getSuperAdminsPermissions();
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());

        Set<String> setOfNameValuesFromResponse = new HashSet<>(response.jsonPath().getList("response.name"));
        Set<String> setOfNameValuesFromDB = new HashSet<>(DatabaseUtility.getColumNameFromTableWhereValueIs("name", "AYS_PERMISSION"));

        Assert.assertEquals(setOfNameValuesFromResponse, setOfNameValuesFromDB, "The names from the response and the database do not match.");
    }
}
