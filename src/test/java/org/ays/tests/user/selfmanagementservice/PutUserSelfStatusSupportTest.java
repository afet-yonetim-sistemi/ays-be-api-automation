package org.ays.tests.user.selfmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.payload.UserCredentials;
import org.ays.payload.UserSupportStatus;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PutUserSelfStatusSupportTest {
    UserCredentials userCredentials;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        userCredentials = InstitutionEndpoints.generateANewUser();
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

}
