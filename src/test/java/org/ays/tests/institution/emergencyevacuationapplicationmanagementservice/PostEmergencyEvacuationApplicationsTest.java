package org.ays.tests.institution.emergencyevacuationapplicationmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.LandingEndpoints;
import org.ays.payload.EmergencyEvacuationApplication;
import org.ays.payload.ListEmergencyEvacuationApplications;
import org.ays.payload.Orders;
import org.ays.payload.Pageable;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostEmergencyEvacuationApplicationsTest {

    @Test(groups = {"Smoke", "Regression"})
    public void testListingEmergencyEvacuationApplications() {
        ListEmergencyEvacuationApplications list = new ListEmergencyEvacuationApplications();
        list.setPageable(Pageable.generateFirstPage());

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUnfilteredListResponseSpec())
                .body("response.content.id", everyItem(notNullValue()))
                .body("response.content.referenceNumber", everyItem(notNullValue()))
                .body("response.content.firstName", everyItem(notNullValue()))
                .body("response.content.lastName", everyItem(notNullValue()))
                .body("response.content.phoneNumber.countryCode", everyItem(notNullValue()))
                .body("response.content.phoneNumber.lineNumber", everyItem(notNullValue()))
                .body("response.content.seatingCount", everyItem(notNullValue()))
                .body("response.content.status", everyItem(notNullValue()))
                .body("response.content.isInPerson", everyItem(notNullValue()))
                .body("response.content.createdAt", everyItem(notNullValue()));
    }

    @Test(groups = {"Smoke", "Regression"})
    public void testFilteringEvacuationApplicationsWithAnExistingApplicationData() {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        LandingEndpoints.postEmergencyEvacuationApplication(application);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(ListEmergencyEvacuationApplications.generate(application));
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].referenceNumber", equalTo(DatabaseUtility.getLatestReferenceNumber()))
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
                .body("response.filteredBy.referenceNumber", equalTo(DatabaseUtility.getLatestReferenceNumber()))
                .body("response.filteredBy.sourceCity", equalTo(application.getSourceCity()))
                .body("response.filteredBy.sourceDistrict", equalTo(application.getSourceDistrict()))
                .body("response.filteredBy.seatingCount", equalTo(application.getSeatingCount()))
                .body("response.filteredBy.targetCity", equalTo(application.getTargetCity()))
                .body("response.filteredBy.targetDistrict", equalTo(application.getTargetDistrict()))
                .body("response.filteredBy.statuses[0]", equalTo("PENDING"))
                .body("response.filteredBy.isInPerson", equalTo(true));
    }

    @Test(groups = {"Regression", "Institution"})
    public void testFilteringEvacuationApplicationsWithInvalidReferenceNumber() {

        String referenceNumber = "asdfghjkliu";
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setReferenceNumber(referenceNumber);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("size must be between 1 and 10"))
                .body("subErrors[0].field", equalTo("referenceNumber"))
                .body("subErrors[0].value", equalTo(referenceNumber))
                .body("subErrors[0].type", equalTo("String"));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePaginationData", dataProviderClass = DataProvider.class)
    public void testListingEvacuationApplicationsWithInvalidPagination(int page, int pageSize) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getPageable().setPage(page);
        list.getPageable().setPageSize(pageSize);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectInvalidPaginationErrors());
    }

    @Test(groups = {"Smoke", "Institution", "Regression"}, dataProvider = "positivePaginationData", dataProviderClass = DataProvider.class)
    public void testListingEvacuationApplicationsWithValidPagination(int page, int pageSize) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getPageable().setPage(page);
        list.getPageable().setPageSize(pageSize);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidCity(String value, ErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setSourceCity(value);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDistrictDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringApplicationsWithInvalidSourceDistrict(String value, ErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setSourceDistrict(value);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidSeatingCountData", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidSeatingCount(Integer value, ErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setSeatingCount(value);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDistrictDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidTargetDistrict(String value, ErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setSourceDistrict(value);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidTargetCity(String value, ErrorMessage errorMessage, String field, String fieldType) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setSourceCity(value);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, fieldType));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithInvalidStatuses(List<String> statuses, ErrorMessage expectedMessage, String field, String type) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setStatuses(statuses);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(expectedMessage, field, type));
    }

    @Test(groups = {"Smoke", "Institution", "Regression"}, dataProvider = "validStatusesDataForFilteringEvacuationApplications", dataProviderClass = DataProvider.class)
    public void testFilteringEvacuationApplicationsWithValidStatusesField(List<String> statuses, List<String> expectedStatuses) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setStatuses(statuses);

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.filteredBy.statuses", containsInAnyOrder(expectedStatuses.toArray()));
    }

    @Test(groups = {"Institution", "Regression"})
    public void testFilteringEvacuationApplicationsWithMultipleInvalidFields() {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getFilter().setTargetDistrict("invalid$%%");
        list.getPageable().setPageSize(-1);
        list.getFilter().setReferenceNumber("12345678908");

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors.message", containsInAnyOrder(
                        "must be between 1 and 99999999",
                        "contains invalid characters",
                        "size must be between 1 and 10"
                ));
    }

    @Test(groups = {"Institution", "Regression"}, dataProvider = "negativeOrderData", dataProviderClass = DataProvider.class)
    public void testListingEvacuationApplicationsWithInvalidSortData(String property, String direction, ErrorMessage errorMessage) {
        EmergencyEvacuationApplication application = EmergencyEvacuationApplication.generateForMe();
        ListEmergencyEvacuationApplications list = ListEmergencyEvacuationApplications.generate(application);
        list.getPageable().setOrders(Orders.generate(property, direction));

        Response response = InstitutionEndpoints.postEmergencyEvacuationApplications(list);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage.getMessage()));
    }
}
