package org.ays.payload;

import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.ays.endpoints.InstitutionAuthEndpoints;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.endpoints.UserAuthEndpoints;
import org.ays.endpoints.UserEndpoints;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@UtilityClass
@Slf4j
public class Helper {


    public static String extractAssignmentIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listAssignments(RequestBodyAssignments.generateRequestBodyAssignmentsWithPhoneNumberFilter(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");

    }

    public static String extractUserIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listUsers(createRequestBodyUsersWithPhoneNumberFilter(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");
    }

    public static RequestBodyUsers createRequestBodyUsersWithPhoneNumberFilter(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPagination(Pagination.createPagination());
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

    public static void setSupportStatus(String status, String username, String password) {
        String payload = createPayloadWithSupportStatus(status);
        UserEndpoints.updateSupportStatus(payload, username, password);
    }

    public static String createPayloadWithSupportStatus(String supportStatus) {
        return "{\n" +
                "    \"supportStatus\": \"" + supportStatus + "\"\n" +
                "}";
    }

    public static FiltersForUsers createFilterWithUserFirstAndLastName(String firstname, String lastname) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        return filters;
    }

    public static FiltersForUsers createFilterWithUserStatus(String status) {
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(status);
        filters.setStatuses(statuses);
        return filters;
    }

    public static FiltersForUsers createFilterWithUserSupportStatus(String supportStatus) {
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(supportStatus);
        filters.setSupportStatuses(statuses);
        return filters;
    }

    public static FiltersForUsers createFilterWithUserPhoneNumber(PhoneNumber phoneNumber) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForUsers createFilterWithAllUserFilters(PhoneNumber phoneNumber, String firstname, String lastname, String status, String supportStatus) {
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        List<String> statuses = Arrays.asList(status);
        filters.setStatuses(statuses);
        List<String> supportStatuses = Arrays.asList(supportStatus);
        filters.setSupportStatuses(supportStatuses);
        return filters;
    }

    public static FiltersForAssignments createFilterWithAssignmentPhoneNumber(PhoneNumber phoneNumber) {
        FiltersForAssignments filters = new FiltersForAssignments();
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments createFilterWithAssignmentStatus(String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        return filters;
    }

    public static FiltersForAssignments createFilterWithAssignmentStatusAndLineNumber(String lineNumber, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber(lineNumber);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments createFilterWithAssignmentStatusAndCountryCOde(String countryCode, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static FiltersForAssignments createFilterWithAssignmentStatusPhoneNumber(PhoneNumber phoneNumber, String... statusesToAdd) {
        FiltersForAssignments filters = new FiltersForAssignments();
        List<String> statuses = new ArrayList<>();
        if (statusesToAdd != null) {
            Collections.addAll(statuses, statusesToAdd);
        }
        filters.setStatuses(statuses);
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }

    public static List<Sort> createSortBody(String property, String direction) {
        Sort sort = new Sort();
        sort.setDirection(direction);
        sort.setProperty(property);
        List<Sort> sortList = Arrays.asList(sort);
        return sortList;
    }

    public static String getApplicationID() {
        ApplicationRegistration applicationRegistration = new ApplicationRegistration();
        applicationRegistration.setReason("A valid test string must have forty character.");
        applicationRegistration.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        while (true) {
            Response response = InstitutionEndpoints.postRegistrationAdminApplication(applicationRegistration);
            String applicationID = response.jsonPath().getString("response.id");
            if (applicationID != null) {
                return applicationID;
            }
        }
    }

    public static String getApplicationId() {
        RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
        requestBodyInstitution.setPagination(Pagination.setPagination(1, 10));
        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        List<Map<String, Object>> content = response.jsonPath().getList("response.content");
        if (content != null && !content.isEmpty()) {
            String applicationId = (String) content.get(0).get("id");
            if (applicationId != null) {
                return applicationId;
            } else {
                throw new RuntimeException("Application ID is null");
            }
        } else {
            throw new RuntimeException("No registration applications found");
        }
    }

    public static String getAdminRegistrationApplicationID() {
        ApplicationRegistration applicationRegistration = new ApplicationRegistration();
        applicationRegistration.setReason("A valid test string must have forty character.");
        applicationRegistration.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        Response response = InstitutionEndpoints.postRegistrationAdminApplication(applicationRegistration);
        if (response != null) {
            return response.jsonPath().getString("response.id");

        } else {
            throw new RuntimeException("Registration application creation failed with status code: " + response.getStatusCode());
        }
    }

    public static ApplicationRegistration generateApplicationRegistrationPayload() {
        ApplicationRegistration application = new ApplicationRegistration();
        application.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        application.setReason(AysRandomUtil.generateReasonString());
        return application;
    }

    public static ApplicationRegistration generateApplicationRegistrationPayloadWithoutReason() {
        ApplicationRegistration application = new ApplicationRegistration();
        application.setInstitutionId(AysConfigurationProperty.InstitutionOne.ID);
        return application;
    }

    public static String getAdminRefreshToken(AdminCredentials adminCredentials) {
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        return response.jsonPath().getString("response.refreshToken");
    }

    public static String getUserRefreshToken(UserCredentials userCredentials) {
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        return response.jsonPath().getString("response.refreshToken");
    }

    public static Token getAdminToken(AdminCredentials adminCredentials) {
        Token token = new Token();
        Response response = InstitutionAuthEndpoints.getAdminToken(adminCredentials);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
        return token;
    }

    public static Token getUserToken(UserCredentials userCredentials) {
        Token token = new Token();
        Response response = UserAuthEndpoints.getUserToken(userCredentials);
        token.setAccessToken(response.jsonPath().getString("response.accessToken"));
        token.setRefreshToken(response.jsonPath().getString("response.refreshToken"));
        return token;
    }

    public static AdminCredentials setIntsAdminCredentials() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setUsername(AysConfigurationProperty.InstitutionOne.AdminUserOne.USERNAME);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);
        return adminCredentials;
    }

    public static List<Map<String, String>> extractResponseAsList(Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("response");
    }

}

