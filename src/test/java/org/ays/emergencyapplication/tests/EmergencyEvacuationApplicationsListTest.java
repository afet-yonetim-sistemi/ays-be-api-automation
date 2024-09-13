package org.ays.emergencyapplication.tests;

import io.restassured.response.Response;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.emergencyapplication.datasource.EmergencyEvacuationApplicationDataSource;
import org.ays.emergencyapplication.endpoints.EmergencyEvacuationApplicationEndpoints;
import org.ays.emergencyapplication.model.payload.EmergencyEvacuationApplicationListPayload;
import org.ays.emergencyapplication.model.payload.EmergencyEvacuationApplicationPayload;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class EmergencyEvacuationApplicationsListTest {

    @Test(groups = {"Smoke", "Regression"})
    public void testListingEmergencyEvacuationApplications() {
        EmergencyEvacuationApplicationListPayload list = new EmergencyEvacuationApplicationListPayload();
        list.setPageable(AysPageable.generateFirstPage());

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUnfilteredListResponseSpecForEmergencyEvacuationApplications())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.content.id", everyItem(notNullValue()))
                .body("response.content.referenceNumber", everyItem(notNullValue()))
                .body("response.content.firstName", everyItem(notNullValue()))
                .body("response.content.lastName", everyItem(notNullValue()))
                .body("response.content.phoneNumber.countryCode", everyItem(notNullValue()))
                .body("response.content.phoneNumber.lineNumber", everyItem(notNullValue()))
                .body("response.content.seatingCount", everyItem(notNullValue()))
                .body("response.content.status", everyItem(notNullValue()))
                .body("response.content.isInPerson", everyItem(notNullValue()))
                .body("response.content.createdAt", everyItem(notNullValue()))
                .body("response.filteredBy.institutionId", notNullValue());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void testFilteringEvacuationApplicationsWithAnExistingApplicationData() {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplication(application);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(EmergencyEvacuationApplicationListPayload.generate(application));
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].referenceNumber", equalTo(EmergencyEvacuationApplicationDataSource.findLastCreatedReferenceNumber()))
                .body("response.content[0].firstName", equalTo(application.getFirstName()))
                .body("response.content[0].lastName", equalTo(application.getLastName()))
                .body("response.content[0].phoneNumber.countryCode", equalTo(application.getPhoneNumber().getCountryCode()))
                .body("response.content[0].phoneNumber.lineNumber", equalTo(application.getPhoneNumber().getLineNumber()))
                .body("response.content[0].seatingCount", equalTo(application.getSeatingCount()))
                .body("response.content[0].status", equalTo("PENDING"))
                .body("response.content[0].isInPerson", is(true))
                .body("response.content[0].createdAt", notNullValue())
                .body("response.pageNumber", equalTo(1))
                .body("response.pageSize", equalTo(1))
                .body("response.totalPageCount", equalTo(1))
                .body("response.totalElementCount", equalTo(1))
                .body("response.orderedBy[0].property", equalTo("createdAt"))
                .body("response.orderedBy[0].direction", equalTo("DESC"))
                .body("response.filteredBy.referenceNumber", equalTo(EmergencyEvacuationApplicationDataSource.findLastCreatedReferenceNumber()))
                .body("response.filteredBy.sourceCity", equalTo(application.getSourceCity()))
                .body("response.filteredBy.sourceDistrict", equalTo(application.getSourceDistrict()))
                .body("response.filteredBy.seatingCount", equalTo(application.getSeatingCount()))
                .body("response.filteredBy.targetCity", equalTo(application.getTargetCity()))
                .body("response.filteredBy.targetDistrict", equalTo(application.getTargetDistrict()))
                .body("response.filteredBy.statuses[0]", equalTo("PENDING"))
                .body("response.filteredBy.isInPerson", equalTo(true));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativeReferenceNumberData", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidReferenceNumber(String field, String value, String type, AysErrorMessage errorMessage) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setReferenceNumber(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type))
                .body("subErrors[0].value", equalTo(value));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePageableData", dataProviderClass = AysDataProvider.class)
    public void testListingEvacuationApplicationsWithInvalidPageable(int page, int pageSize) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getPageable().setPage(page);
        list.getPageable().setPageSize(pageSize);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectInvalidPageableErrors());
    }

    @Test(groups = {"Smoke", "Institution", "Regression"}, dataProvider = "positivePageableData", dataProviderClass = AysDataProvider.class)
    public void testListingEvacuationApplicationsWithValidPageable(int page, int pageSize) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getPageable().setPage(page);
        list.getPageable().setPageSize(pageSize);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setSourceCity(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDistrictDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringApplicationsWithInvalidSourceDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setSourceDistrict(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidSeatingCountData", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidSeatingCount(Integer value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setSeatingCount(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDistrictDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidTargetDistrict(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setSourceDistrict(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidTargetCity(String value, AysErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setSourceCity(value);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidStatuses(List<String> statuses, AysErrorMessage expectedMessage, String field, String type) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setStatuses(statuses);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(expectedMessage, field, type));
    }

    @Test(groups = {"Smoke", "Institution", "Regression"}, dataProvider = "validStatusesDataForFilteringEvacuationApplications", dataProviderClass = AysDataProvider.class)
    public void testFilteringEvacuationApplicationsWithValidStatusesField(List<String> statuses, List<String> expectedStatuses) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setStatuses(statuses);

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.filteredBy.statuses", containsInAnyOrder(expectedStatuses.toArray()));
    }

    @Test(groups = {"Institution", "Regression"})
    public void testFilteringEvacuationApplicationsWithMultipleInvalidFields() {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getFilter().setTargetDistrict("invalid$%%");
        list.getPageable().setPageSize(-1);
        list.getFilter().setReferenceNumber("12345678908");

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors.message", containsInAnyOrder(
                        "must be between 1 and 99999999",
                        "contains invalid characters",
                        "size must be between 1 and 10"
                ));
    }

    @Test(groups = {"Institution", "Regression"}, dataProvider = "negativeOrderData", dataProviderClass = AysDataProvider.class)
    public void testListingEvacuationApplicationsWithInvalidSortData(String property, String direction, AysErrorMessage errorMessage) {
        EmergencyEvacuationApplicationPayload application = EmergencyEvacuationApplicationPayload.generateForMe();
        EmergencyEvacuationApplicationListPayload list = EmergencyEvacuationApplicationListPayload.generate(application);
        list.getPageable().setOrders(AysOrder.generate(property, direction));

        Response response = EmergencyEvacuationApplicationEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()));
    }
}