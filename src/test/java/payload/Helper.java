package payload;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;

import java.util.*;

public class Helper {
    public static User getUser(String userID) {
        Response response = InstitutionEndpoints.getUser(userID);
        if (response.getStatusCode() == 200) {
            if (response.jsonPath().get("response") != null) {
                return response.then()
                        .extract()
                        .jsonPath()
                        .getObject("response", User.class);
            } else {
                throw new RuntimeException("User data not found for userID: " + userID);
            }
        } else {
            throw new RuntimeException("Failed to retrieve user with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials createNewUser() {
        User user = createUserPayload();
        Response response = InstitutionEndpoints.createAUser(user);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials createNewUser(User userPayload) {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static User createUserPayload() {
        Faker faker = new Faker();
        User userPayload = new User();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setPhoneNumber(createPhoneNumber());
        return userPayload;
    }

    public static Map<String, String> createLoginPayload(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);
        return payload;
    }

    public static RequestBodyAssignments createRequestBodyAssignments(int page, int pageSize) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }
    public static RequestBodyUsers createRequestBodyUsers(int page, int pageSize) {
        RequestBodyUsers requestBodyUsers=new RequestBodyUsers();
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyUsers.setPagination(pagination);
        return requestBodyUsers;
    }

    public static Assignment createANewAssignment() {
        Assignment assignment = createAssignmentPayload();
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        if (response.getStatusCode() == 200) {
            return assignment;
        } else {
            throw new RuntimeException("Assignment creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Assignment createAssignmentPayload() {
        Faker faker = new Faker();
        Assignment assignment = new Assignment();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());
        assignment.setPhoneNumber(createPhoneNumber());
        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(generateRandomCoordinate(38, 40));
        assignment.setLongitude(generateRandomCoordinate(28, 43));
        return assignment;
    }

    public static PhoneNumber createPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        return phoneNumber;


    }

    public static RequestBodyAssignments createRequestBodyAssignmentsMissingPagination() {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }

    public static String generateLineNumber() {
        Random random = new Random();
        StringBuilder phoneNumberBuilder = new StringBuilder();
        phoneNumberBuilder.append("535");
        for (int i = 0; i < 7; i++) {
            int digit = random.nextInt(10);
            phoneNumberBuilder.append(digit);
        }
        return phoneNumberBuilder.toString();
    }

    public static String extractAssignmentIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listAssignments(createRequestBodyAssignmentsWithPhoneNumberFilter(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");

    }

    public static String extractUserIdByPhoneNumber(PhoneNumber phoneNumber) {
        Response response = InstitutionEndpoints.listUsers(createRequestBodyUsersWithPhoneNumberFilter(phoneNumber));
        return response.jsonPath().getString("response.content[0].id");
    }

    public static RequestBodyUsers createRequestBodyUsersWithPhoneNumberFilter(PhoneNumber phoneNumber) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPagination(createPagination());
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        requestBodyUsers.setFilter(filters);
        return requestBodyUsers;
    }

    public static RequestBodyAssignments createRequestBodyAssignmentsWithPhoneNumberFilter(PhoneNumber phoneNumber) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();

        requestBodyAssignments.setPagination(createPagination());
        FiltersForAssignments filters = new FiltersForAssignments();
        filters.setPhoneNumber(phoneNumber);
        requestBodyAssignments.setFilter(filters);
        return requestBodyAssignments;
    }
    public static Pagination createPagination(){
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setPageSize(10);
        return pagination;
    }


    public static String getUserIDByFirstName(String firstName) {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        Pagination pagination = setPagination(1, 10);
        requestBodyUsers.setPagination(setPagination(1, 10));
        int currentPage = 1;
        while (true) {
            Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
            String userID = extractUserIdFromTheUserListByFirstname(response, firstName);
            if (userID != null) {
                return userID;
            }
            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            int totalPageCount = response.jsonPath().getInt("response.totalPageCount");
            if (currentPageNumber >= totalPageCount) {
                break;
            }
            pagination.setPage(currentPage + 1);
            requestBodyUsers.setPagination(pagination);
            currentPage++;
        }

        return null;
    }

    public static Pagination setPagination(int page, int pageSize) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(pageSize);
        pagination.setPage(page);
        return pagination;
    }

    private static String extractUserIdFromTheUserListByFirstname(Response response, String firstname) {
        List<Map<String, Object>> userList = response.jsonPath().getList("response.content");
        for (Map<String, Object> user : userList) {
            String userFirstName = (String) user.get("firstName");
            if (firstname.equals(userFirstName)) {
                return (String) user.get("id");
            }
        }
        return null;
    }

    public static Location generateLocation(double minLat, double maxLat, double minLon, double maxLon) {
        Random rand = new Random();
        double latitude = minLat + (maxLat - minLat) * rand.nextDouble();
        double longitude = minLon + (maxLon - minLon) * rand.nextDouble();
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public static Location generateLocation(Double longitude, Double latitude) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


    public static Location generateLocationTR() {
        Random rand = new Random();
        double latitude = 38 + (40 - 38) * rand.nextDouble();
        double longitude = 28 + (43 - 28) * rand.nextDouble();
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
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

    private static double generateRandomCoordinate(int min, int max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public static String generateString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            builder.append(randomChar);
        }

        return builder.toString();
    }

    public static String generateInvalidLineNumber() {
        int[] prefixesArray = {212, 216, 222, 224, 226, 228, 232, 236, 242, 246, 248, 252, 256, 258, 262, 264, 266, 272, 274, 276, 282, 284, 286, 288, 312, 318, 322, 324, 326, 328, 332, 338, 342, 344, 346, 348, 352, 354, 356, 358, 362, 364, 366, 368, 370, 372, 374, 376, 378, 380, 382, 384, 386, 388, 392, 412, 414, 416, 422, 424, 426, 428, 432, 434, 436, 438, 442, 446, 452, 454, 456, 458, 462, 464, 466, 472, 474, 476, 478, 482, 484, 486, 488};
        Random random = new Random();
        String phoneNumber;
        do {
            int firstThreeDigits = random.nextInt(900) + 100;
            phoneNumber = String.valueOf(firstThreeDigits) + generateRandomDigits(7);
        } while (isPrefixInArray(Integer.parseInt(phoneNumber.substring(0, 3)), prefixesArray));
        return phoneNumber;
    }

    public static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static boolean isPrefixInArray(int prefix, int[] prefixesArray) {
        for (int i : prefixesArray) {
            if (i == prefix) {
                return true;
            }
        }
        return false;
    }
    public static FiltersForUsers createFilterWithUserFirstAndLastName(String firstname, String lastname){
        FiltersForUsers filters = new FiltersForUsers();
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        return filters;
    }
    public static FiltersForUsers createFilterWithUserStatus(String status){
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(status);
        filters.setStatuses(statuses);
        return filters;
    }
    public static FiltersForUsers createFilterWithUserSupportStatus(String supportStatus){
        FiltersForUsers filters = new FiltersForUsers();
        List<String> statuses = Arrays.asList(supportStatus);
        filters.setSupportStatuses(statuses);
        return filters;
    }
    public static FiltersForUsers createFilterWithUserPhoneNumber(PhoneNumber phoneNumber){
        FiltersForUsers filters = new FiltersForUsers();
        filters.setPhoneNumber(phoneNumber);
        return filters;
    }
    public static FiltersForUsers createFilterWithAllUserFilters(PhoneNumber phoneNumber,String firstname,String lastname,String status, String supportStatus){
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
    public static List<Sort> createSortBody(String property,String direction){
        Sort sort = new Sort();
        sort.setDirection(direction);
        sort.setProperty(property);
        List<Sort> sortList = Arrays.asList(sort);
        return sortList;
    }

    public static String getAdminID() {
        RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
        Pagination pagination = setPagination(1, 10);
        requestBodyInstitution.setPagination(setPagination(1, 10));
        int currentPage = 1;
        while (true) {
            Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution,"SUPER_ADMIN");
            String adminID = response.jsonPath().getString("response.content[0].id");
            if (adminID != null) {
                return adminID;
            }

        }

    }

}

