package org.ays.tests.user.selfmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.Location;
import org.ays.payload.UserCredentials;
import org.ays.payload.UserSupportStatus;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class PutUserSelfStatusSupportTest {
    UserCredentials userCredentials;
    Location location;
    Assignment assignment;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = InstitutionEndpoints.generateANewUser();
        location = Location.generateForTurkey();
        assignment = InstitutionEndpoints.generateANewAssignment();
    }

    @Test(groups = {"Smoke", "Regression", "User"}, dataProvider = "statusTransitions", dataProviderClass = DataProvider.class)
    public void updateSupportStatus(String toStatus) {

        UserSupportStatus userSupportStatus = UserSupportStatus.valueOf(toStatus);
        Response response = UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(userSupportStatus),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test()
    public void updateSupportStatusAfterReserveAnAssignment() {

        UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.READY),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        UserEndpoints.searchAssignment(location, userCredentials.getUsername(), userCredentials.getPassword());

        Response response = UserEndpoints.updateSupportStatus(
                new UserSupportStatusUpdatePayload(UserSupportStatus.IDLE),
                userCredentials.getUsername(),
                userCredentials.getPassword()
        );
        response.then()
                .spec(AysResponseSpecs.expectConflictResponseSpec())
                .body("message", containsString("USER CANNOT UPDATE SUPPORT STATUS BECAUSE USER HAS ASSIGNMENT!"));

    }

}
