package org.ays.emergencyapplication.tests;

import io.restassured.response.Response;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.emergencyapplication.endpoints.EmergencyEvacuationApplicationEndpoints;
import org.ays.emergencyapplication.model.payload.EmergencyEvacuationApplicationPayload;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.AllOf.allOf;

public class EmergencyEvacuationApplicationCreateTest {

    @Test(groups = {"Smoke", "Regression"})
    public void createEmergencyEvacuationApplicationForMe() {
        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForMe();
        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void createEmergencyEvacuationApplicationForOtherPerson() {
        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForOtherPerson();
        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidName", dataProviderClass = AysDataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidName(String field, String value, AysErrorMessage errorMessage, String type) {
        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForMe();

        if ("firstName".equals(field)) {
            emergencyEvacuationApplicationPayload.setFirstName(value);
        } else if ("lastName".equals(field)) {
            emergencyEvacuationApplicationPayload.setLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression"}, dataProvider = "blankApplicantName", dataProviderClass = AysDataProvider.class)
    public void createEmergencyEvacuationApplicationWithBlankApplicantName(String field, String value, AysErrorMessage errorMessage) {
        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForOtherPerson();

        if ("applicantFirstName".equals(field)) {
            emergencyEvacuationApplicationPayload.setApplicantFirstName(value);
        } else if ("applicantLastName".equals(field)) {
            emergencyEvacuationApplicationPayload.setApplicantLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())
                ));
    }


    @Test(groups = {"Regression"}, dataProvider = "applicantName", dataProviderClass = AysDataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidApplicantName(String field, String value, AysErrorMessage errorMessage, String type) {
        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForOtherPerson();

        if ("applicantFirstName".equals(field)) {
            emergencyEvacuationApplicationPayload.setApplicantFirstName(value);
        } else if ("applicantLastName".equals(field)) {
            emergencyEvacuationApplicationPayload.setApplicantLastName(value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()))
                .body("subErrors[0].field", equalTo(field))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        testInvalidPhoneNumber(countryCode, lineNumber, false, errorMessage, field, type);
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPhoneNumberData", dataProviderClass = AysDataProvider.class)
    public void createEmergencyEvacuationApplicationWithInvalidApplicantPhoneNumber(String countryCode, String lineNumber, AysErrorMessage errorMessage, String field, String type) {
        testInvalidPhoneNumber(countryCode, lineNumber, true, errorMessage, field, type);
    }

    private void testInvalidPhoneNumber(String countryCode, String lineNumber, boolean isApplicant, AysErrorMessage errorMessage, String field, String type) {
        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);

        EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload;
        if (isApplicant) {
            emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForOtherPerson();
            emergencyEvacuationApplicationPayload.setApplicantPhoneNumber(phoneNumber);
        } else {
            emergencyEvacuationApplicationPayload = EmergencyEvacuationApplicationPayload.generateForMe();
            emergencyEvacuationApplicationPayload.setPhoneNumber(phoneNumber);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.create(emergencyEvacuationApplicationPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()))
                .body("subErrors[0].type", equalTo(type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidSourceCityData", dataProviderClass = AysDataProvider.class)
    public void testInvalidSourceCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidSourceDistrictData", dataProviderClass = AysDataProvider.class)
    public void testInvalidSourceDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidAddressData", dataProviderClass = AysDataProvider.class)
    public void testInvalidAddress(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        application.setAddress(value);

        Response response = EmergencyEvacuationApplicationEndpoints.create(application);

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(allOf(
                        hasEntry("message", errorMessage.getMessage()),
                        hasEntry("field", field),
                        hasEntry("type", fieldType)
                )));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidSeatingCountData", dataProviderClass = AysDataProvider.class)
    public void testInvalidSeatingCount(Integer value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidTargetCityData", dataProviderClass = AysDataProvider.class)
    public void testInvalidTargetCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidTargetDistrictData", dataProviderClass = AysDataProvider.class)
    public void testInvalidTargetDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        validateField(field, value, errorMessage, fieldType);
    }

    private void validateField(String fieldName, Object value, AysErrorMessage errorMessage, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        switch (fieldName) {
            case "sourceCity" -> application.setSourceCity((String) value);
            case "sourceDistrict" -> application.setSourceDistrict((String) value);
            case "address" -> application.setAddress((String) value);
            case "seatingCount" -> application.setSeatingCount((Integer) value);
            case "targetCity" -> application.setTargetCity((String) value);
            case "targetDistrict" -> application.setTargetDistrict((String) value);
        }

        Response response = EmergencyEvacuationApplicationEndpoints.create(application);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors", hasItem(
                        hasEntry("message", errorMessage.getMessage())))
                .body("subErrors[0].field", equalTo(fieldName))
                .body("subErrors[0].type", equalTo(fieldType));
    }


}
