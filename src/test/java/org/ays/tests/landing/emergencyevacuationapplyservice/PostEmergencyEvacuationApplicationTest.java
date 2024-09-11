package org.ays.tests.landing.emergencyevacuationapplyservice;

import io.restassured.response.Response;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.emergencyapplication.endpoints.EmergencyEvacuationApplicationEndpoints;
import org.ays.payload.EmergencyEvacuationApplication;
import org.ays.payload.PhoneNumber;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.AllOf.allOf;

public class PostEmergencyEvacuationApplicationTest {

    @Test(groups = {"Smoke", "Regression", "Landing"})
    public void createEmergencyEvacuationApplicationForMe() {
        EmergencyEvacuationApplication emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForMe();
        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "Landing"})
    public void createEmergencyEvacuationApplicationForOtherPerson() {
        EmergencyEvacuationApplication emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForOtherPerson();
        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidName", dataProviderClass = DataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidName(String field, String value, AysErrorMessage errorMessage, String type) {
        EmergencyEvacuationApplication emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForMe();

        if ("firstName".equals(field)) {
            emergencyEvacuationApplication.setFirstName(value);
        } else if ("lastName".equals(field)) {
            emergencyEvacuationApplication.setLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "blankApplicantName", dataProviderClass = DataProvider.class)
    public void createEmergencyEvacuationApplicationWithBlankApplicantName(String field, String value, AysErrorMessage errorMessage) {
        EmergencyEvacuationApplication emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForOtherPerson();

        if ("applicantFirstName".equals(field)) {
            emergencyEvacuationApplication.setApplicantFirstName(value);
        } else if ("applicantLastName".equals(field)) {
            emergencyEvacuationApplication.setApplicantLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())
                ));
    }


    @Test(groups = {"Regression", "Landing"}, dataProvider = "applicantName", dataProviderClass = DataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidApplicantName(String field, String value, AysErrorMessage errorMessage, String type) {
        EmergencyEvacuationApplication emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForOtherPerson();

        if ("applicantFirstName".equals(field)) {
            emergencyEvacuationApplication.setApplicantFirstName(value);
        } else if ("applicantLastName".equals(field)) {
            emergencyEvacuationApplication.setApplicantLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        testInvalidPhoneNumber(countryCode, lineNumber, false, errorMessage, field, type);
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = DataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidApplicantPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        testInvalidPhoneNumber(countryCode, lineNumber, true, errorMessage, field, type);
    }

    private void testInvalidPhoneNumber(String countryCode, String lineNumber, boolean isApplicant, AysErrorMessage errorMessage, String field, String type) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);

        EmergencyEvacuationApplication emergencyEvacuationApplication;
        if (isApplicant) {
            emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForOtherPerson();
            emergencyEvacuationApplication.setApplicantPhoneNumber(phoneNumber);
        } else {
            emergencyEvacuationApplication = EmergencyEvacuationApplication.generateForMe();
            emergencyEvacuationApplication.setPhoneNumber(phoneNumber);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(emergencyEvacuationApplication);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidSourceCityData", dataProviderClass = DataProvider.class)
    public void testInvalidSourceCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidSourceDistrictData", dataProviderClass = DataProvider.class)
    public void testInvalidSourceDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidAddressData", dataProviderClass = DataProvider.class)
    public void testInvalidAddress(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        application.setAddress(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(application);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(allOf(
                        hasEntry("message", errorMessage.getMessage()),
                        hasEntry("field", field),
                        hasEntry("type", fieldType)
                )));
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidSeatingCountData", dataProviderClass = DataProvider.class)
    public void testInvalidSeatingCount(Integer value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidTargetCityData", dataProviderClass = DataProvider.class)
    public void testInvalidTargetCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression", "Landing"}, dataProvider = "invalidTargetDistrictData", dataProviderClass = DataProvider.class)
    public void testInvalidTargetDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    private void validateField(String fieldName, Object value, AysErrorMessage errorMessage, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        switch (fieldName) {
            case "sourceCity" -> application.setSourceCity((String) value);
            case "sourceDistrict" -> application.setSourceDistrict((String) value);
            case "address" -> application.setAddress((String) value);
            case "seatingCount" -> application.setSeatingCount((Integer) value);
            case "targetCity" -> application.setTargetCity((String) value);
            case "targetDistrict" -> application.setTargetDistrict((String) value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo(fieldName))
                .body("subErrors[0].type", equalTo(fieldType));
    }


}
