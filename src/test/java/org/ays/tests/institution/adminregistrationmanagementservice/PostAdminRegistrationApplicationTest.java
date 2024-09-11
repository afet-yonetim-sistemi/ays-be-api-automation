package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.payload.AdminRegistrationApplicationCreatePayload;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class PostAdminRegistrationApplicationTest {
    AdminRegistrationApplicationCreatePayload application;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        application = new AdminRegistrationApplicationCreatePayload();
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplication() {
        application = AdminRegistrationApplicationCreatePayload.generate(AysConfigurationProperty.InstitutionOne.ID, AysRandomUtil.generateReasonString());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response", hasKey("id"));
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "invalidDataForPostApplicationReasonField", dataProviderClass = DataProvider.class, enabled = false)
    public void createAnAdminRegistrationApplicationWithInvalidInputs(String reason, String message, String field, String type) {
        application = AdminRegistrationApplicationCreatePayload.generate(AysConfigurationProperty.InstitutionOne.ID, reason);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", anyOf(containsString(message), containsString("must not be blank")))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void createAnAdminRegistrationApplicationWithInvalidInstitutionId() {
        application = AdminRegistrationApplicationCreatePayload.generate("invalidId", AysRandomUtil.generateReasonString());
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectNotFoundResponseSpec())
                .body("message", containsString("INSTITUTION NOT EXIST!"));
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a Super Admin when I create an admin registration application with missing institution ID I want to get a proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void createAnAdminRegistrationApplicationWithMissingInstitutionId() {
        application = AdminRegistrationApplicationCreatePayload.generate(null, AysRandomUtil.generateReasonString());
        application.setInstitutionId(null);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationAdminApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must not be blank"))
                .body("subErrors[0].field", equalTo("institutionId"))
                .body("subErrors[0].type", equalTo("String"));
    }


}
