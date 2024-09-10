package org.ays.utility;

import lombok.experimental.UtilityClass;
import org.ays.common.enums.ErrorMessage;
import org.ays.payload.UserSupportStatus;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class DataProvider {
    @org.testng.annotations.DataProvider(name = "positivePageableData")
    public static Object[][] positivePageableData() {
        return new Object[][]{
                {1, 10},
                {99999999, 1},
                {50, 50},
                {1000, 20}
        };
    }

    @org.testng.annotations.DataProvider(name = "negativePageableData")
    public static Object[][] negativePageableData() {
        return new Object[][]{
                {-1, 10},
                {100000000, 10},
                {-5, 10},
                {1, -10},
                {1, 1000000000},
                {1, -5},
                {-100, 100000000},
                {-5, -5},
                {100000000, 100000000},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPageableData")
    public static Object[][] invalidPageableData() {
        return new Object[][]{
                {-1, 10, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "page", "int"},
                {100000000, 10, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "page", "int"},
                {1, -10, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "pageSize", "int"},
                {1, 1000000000, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "pageSize", "int"},
                {-100, 100000000, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "pageSize", "int"},
                {-5, -5, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "pageSize", "int"},
                {100000000, 100000000, ErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "pageSize", "int"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidOrdersData")
    public static Object[][] invalidOrdersData() {
        return new Object[][]{
                {null, "ASC", "must not be null"},
                {"createdAt", null, "must not be null"},
                {"", "ASC", "must be true"},
                {"invalid", "ASC", "must be true"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPropertyData")
    public static Object[][] invalidPropertyData() {
        return new Object[][]{
                {null, ErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"invalid", ErrorMessage.MUST_BE_TRUE, "orderPropertyAccepted", null},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDirectionData")
    public static Object[][] invalidDirectionData() {
        return new Object[][]{
                {null, ErrorMessage.MUST_NOT_BE_NULL, "direction", "AysSort.Direction"},
                {"", ErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
                {"invalid", ErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidNames")
    public static Object[][] invalidNames() {
        return new Object[][]{
                {"Noah", "Patricia1", ErrorMessage.MUST_BE_VALID, "lastName", "String"},
                {".Noah", "Patricia", ErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {" John", "Doe", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "firstName", "String"},
                {"Alice", "Johnson ", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "lastName", "String"},
                {"@#$%", "Johnson", ErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {"Alice", "@#$%", ErrorMessage.MUST_BE_VALID, "lastName", "String"},
                {"123", "Smith", ErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {"A".repeat(101), "Doe", ErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"A", "Doe", ErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"Jane", "B".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"},
                {"Jane", "B", ErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"},
                {"", "Doe", ErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"Jane", "", ErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"}
        };
    }

    @org.testng.annotations.DataProvider
    public static Object[][] invalidPhoneNumberData() {
        return new Object[][]{
                {"90", "2105346789", ErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", " 2367894534", ErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "005457568956", ErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"+90", "5457568956", ErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "+5457568956", ErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"", "5457568956", ErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},
                {"90", "", ErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {"  ", "5457568956", ErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},
                {"90", "          ", ErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {"90", null, ErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {null, "5457568956", ErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},

        };
    }

    @org.testng.annotations.DataProvider
    public static Object[][] invalidDescriptionData() {
        return new Object[][]{
                {"", "must not be blank"},
                {"   ", "must not be blank"},
                {"D", "size must be between 2 and 2048"},
                {null, "must not be blank"},
                {"A".repeat(2049), "size must be between 2 and 2048"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCountryCodeData")
    public static Object[][] countryCodeData() {
        return new Object[][]{
                {null},
                {""},
                {"   "},
                {"090"},
                {"A90"},
                {"+90"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidLineNumberData")
    public static Object[][] lineNumberData() {
        return new Object[][]{
                {AysRandomUtil.generateInvalidLineNumber()},
                {AysRandomUtil.generateLineNumber() + "*"},
                {""}, {"          "},
                {null},
                {AysRandomUtil.generateLineNumber() + "a"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidLongitudeValues")
    public static Object[][] invalidLongitudeValues() {
        return new Object[][]{
                {180.000000001, "must be less than or equal to 180"},
                {-180.000000001, "must be greater than or equal to -180"},
                {-200.0, "must be greater than or equal to -180"},
                {-270.0, "must be greater than or equal to -180"},
                {200.0, "must be less than or equal to 180"},
                {270.0, "must be less than or equal to 180"},
                {180.000000001234, "must be less than or equal to 180"},
                {-180.000000001234, "must be greater than or equal to -180"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidLatitudeValues")
    public static Object[][] invalidLatitudeValues() {
        return new Object[][]{
                {100.0, "must be less than or equal to 90"},
                {-100.0, "must be greater than or equal to -90"},
                {90.000000001, "must be less than or equal to 90"},
                {-90.000000001, "must be greater than or equal to -90"},
                {-200.0, "must be greater than or equal to -90"},
                {200.0, "must be less than or equal to 90"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidFirstNamesAndLastDataForAssignment")
    public static Object[][] invalidFirstAndLastNamesDataForAssignment() {
        return new Object[][]{
                {"", "must not be blank"},
                {null, "must not be blank"},
                {"       ", "must not be blank"},
                {" firstName", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"25", "MUST BE VALID"},
                {"firstName ", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"firstName*", "MUST BE VALID"},
                {"F", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum has been the industrys standard dummy text ever since thes when an unknown printer took a galley of type and scrambled it to make a type specimen book It has su Letrasett", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidRejectReason")
    public static Object[][] invalidRejectReason() {
        return new Object[][]{
                {"A".repeat(39)},
                {"A".repeat(513)}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDataForPostApplicationReasonField")
    public static Object[][] invalidDataForPostApplicationReasonField() {
        return new Object[][]{
                {"reason less then forty", "size must be between 40 and 512", "reason", "String"},
                {null, "must not be blank", "reason", "String"},
                {"       ", "size must be between 40 and 512", "reason", "String"},
                {"A".repeat(513), "size must be between 40 and 512", "reason", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPhoneNumberDataForFilter")
    public static Object[][] invalidPhoneNumberDataForFilter() {
        return new Object[][]{
                {"", "1234567890", "size must be between 1 and 3"},
                {"12345", "1234567890", "size must be between 1 and 3"},
                {"90", "", "size must be between 7 and 15"},
                {"90", "12345", "size must be between 7 and 15"},
                {"90", "1234567890123456", "size must be between 7 and 15"}
        };
    }

    @org.testng.annotations.DataProvider(name = "statusTransitions")
    public static Object[][] statusTransitions() {
        return new Object[][]{
                {UserSupportStatus.READY.toString()},
                {UserSupportStatus.IDLE.toString()},
                {UserSupportStatus.BUSY.toString()},
                {UserSupportStatus.IDLE.toString()},
                {UserSupportStatus.READY.toString()},
                {UserSupportStatus.BUSY.toString()},
                {UserSupportStatus.READY.toString()}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidFirstAndLastNamesDataForAdminRegistration")
    public Object[][] invalidFirstAndLastNamesDataForAdminRegistration() {
        return new Object[][]{
                {"", "must not be blank"},
                {null, "must not be blank"},
                {"       ", "must not be blank"},
                {" firstName", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"25", "MUST BE VALID"},
                {"firstName ", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"firstName*", "MUST BE VALID"},
                {"F", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {".a", "MUST BE VALID"},
                {"Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum has been the industrys standard dummy text ever since thes when an unknown printer took a galley of type and scrambled it to make a type specimen book It has su Letrasett", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmailForAdminRegistration")
    public Object[][] invalidEmailForAdminRegistration() {
        return new Object[][]{
                {"", "must not be blank"},
                {null, "must not be blank"},
                {"  ", "must not be blank"},
                {"abc", "must be valid"},
                {"abcgmail.com", "must be valid"},
                {"abc@gmail", "must be valid"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmail")
    public Object[][] invalidEmail() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"  ", ErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"abc", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"},
                {"abcgmail.com", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"},
                {"abc@gmail", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"},
                {null, ErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPhoneNumberDataForRegistrationComplete")
    public static Object[][] invalidPhoneNumberDataForRegistrationComplete() {
        return new Object[][]{
                {"", "1234567890", "must not be blank", "countryCode", "String"},
                {"12345", "1234567890", "must be valid", "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "", "must not be blank", "lineNumber", "String"},
                {"90", "12345", "must be valid", "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "1234567890123456", "must be valid", "phoneNumber", "AysPhoneNumberRequest"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmailAddressForGetAdminToken")
    public static Object[][] invalidEmailAddressForGetAdminToken() {
        return new Object[][]{
                {"", "must not be blank", "emailAddress", "String"},
                {null, "must not be blank", "emailAddress", "String"},
                {"  ", "must not be blank", "emailAddress", "String"},
                {"abc", "must be valid", "emailAddress", "String"},
                {"abcgmail.com", "must be valid", "emailAddress", "String"},
                {"abc@gmail", "must be valid", "emailAddress", "String"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidName")
    public static Object[][] invalidName() {
        return new Object[][]{
                {"firstName", "", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"firstName", "a", ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"firstName", "a".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"firstName", "   ", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"firstName", "firstName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"firstName", "firstName4", ErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "a", ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"lastName", "a".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"lastName", "   ", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "lastName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "lastName4", ErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "applicantName")
    public static Object[][] applicantName() {
        return new Object[][]{
                {"applicantFirstName", "a", ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantFirstName", "a".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantFirstName", "firstName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "firstName4", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantLastName", "a", ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantLastName", "a".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantLastName", "lastName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "lastName4", ErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "blankApplicantName")
    public static Object[][] blankApplicantName() {
        return new Object[][]{
                {"applicantFirstName", "", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantFirstName", "   ", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantLastName", "", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantLastName", "   ", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED}
        };
    }


    @org.testng.annotations.DataProvider(name = "invalidSourceCityData")
    public static Object[][] invalidSourceCityData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"},
                {"City$Name", ErrorMessage.MUST_BE_VALID, "sourceCity", "String"},
                {"C".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidSourceDistrictData")
    public static Object[][] invalidSourceDistrictData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "sourceDistrict", "String"},
                {"District$Name", ErrorMessage.MUST_BE_VALID, "sourceDistrict", "String"},
                {"D".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "sourceDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidAddressData")
    public static Object[][] invalidAddressData() {
        return new Object[][]{
                {"", ErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"Short", ErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"A".repeat(251), ErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"A", ErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"                      ", ErrorMessage.MUST_NOT_BE_BLANK, "address", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidSeatingCountData")
    public static Object[][] invalidSeatingCountData() {
        return new Object[][]{
                {0, ErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"},
                {-1, ErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"},
                {1000, ErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetCityData")
    public static Object[][] invalidTargetCityData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "targetCity", "String"},
                {"City$Name", ErrorMessage.MUST_BE_VALID, "targetCity", "String"},
                {"C".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "targetCity", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "targetCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForUsersList")
    public static Object[][] invalidCityDataForUsersList() {
        return new Object[][]{
                {"", ErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City$Name", ErrorMessage.MUST_BE_VALID, "city", "String"},
                {"C".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City  ", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"  City", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetDistrictData")
    public static Object[][] invalidTargetDistrictData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "targetDistrict", "String"},
                {"District$Name", ErrorMessage.MUST_BE_VALID, "targetDistrict", "String"},
                {"D".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "targetDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForFilteringEvacuationApplications")
    public static Object[][] invalidCityDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"City$Name", ErrorMessage.CONTAINS_INVALID_CHARACTERS, "sourceCity", "String"},
                {"C".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"C", ErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"", ErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDistrictDataForFilteringEvacuationApplications")
    public static Object[][] invalidDistrictDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"", ErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"District$Name", ErrorMessage.CONTAINS_INVALID_CHARACTERS, "sourceDistrict", "String"},
                {"D".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"D", ErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForFilteringEvacuationApplications")
    public static Object[][] invalidStatusesDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {Arrays.asList("INVALID_STATUS"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {Arrays.asList("INVALID_STATUS1", "INVALID_STATUS2"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {Arrays.asList("PENDING", "INVALID_STATUS"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {Arrays.asList("PENDING".repeat(10)), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {Arrays.asList("PENDING$%^&*"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {Arrays.asList("pending"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"}
        };
    }

    @org.testng.annotations.DataProvider(name = "validStatusesDataForFilteringEvacuationApplications")
    public static Object[][] validStatusesDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {List.of("PENDING"), List.of("PENDING")},
                {List.of("IN_REVIEW"), List.of("IN_REVIEW")},
                {List.of("RECEIVED_FIRST_APPROVE"), List.of("RECEIVED_FIRST_APPROVE")},
                {List.of("RECEIVED_SECOND_APPROVE"), List.of("RECEIVED_SECOND_APPROVE")},
                {List.of("RECEIVED_THIRD_APPROVE"), List.of("RECEIVED_THIRD_APPROVE")},
                {List.of("COMPLETED"), List.of("COMPLETED")},
                {List.of("CANCELLED"), List.of("CANCELLED")},
                {List.of("PENDING", "COMPLETED"), List.of("COMPLETED", "PENDING")},
                {List.of("CANCELLED", "IN_REVIEW"), List.of("IN_REVIEW", "CANCELLED")}
        };
    }

    @org.testng.annotations.DataProvider(name = "negativeOrderData")
    public static Object[][] negativeOrderData() {
        return new Object[][]{
                {null, "ASC", ErrorMessage.MUST_NOT_BE_BLANK},
                {"createdAt", null, ErrorMessage.MUST_NOT_BE_NULL},
                {"", "ASC", ErrorMessage.MUST_NOT_BE_BLANK},
                {"invalid", "DESC", ErrorMessage.MUST_BE_TRUE},
                {"createdAt", "invalid", ErrorMessage.MUST_BE_ACCEPTED_VALUE},
                {"createdAt", "", ErrorMessage.MUST_BE_ACCEPTED_VALUE}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidOrderData")
    public static Object[][] invalidOrderData() {
        return new Object[][]{
                {null, "ASC", ErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"createdAt", null, ErrorMessage.MUST_NOT_BE_NULL, "direction", "AysSort.Direction"},
                {"", "ASC", ErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"createdAt", "invalid", ErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
                {"createdAt", "", ErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"}
        };
    }

    @org.testng.annotations.DataProvider(name = "negativeReferenceNumberData")
    public static Object[][] negativeReferenceNumberData() {
        return new Object[][]{
                {"referenceNumber", "12345678901", "String", ErrorMessage.SIZE_BETWEEN_1_10},
                {"referenceNumber", "abcdefghjkl", "String", ErrorMessage.SIZE_BETWEEN_1_10},
                {"referenceNumber", "1234%^*(*)@", "String", ErrorMessage.SIZE_BETWEEN_1_10}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForUsersList")
    public static Object[][] invalidStatusesDataForUsersList() {
        return new Object[][]{
                {Arrays.asList("INVALID_STATUS"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {Arrays.asList("INVALID_STATUS1", "INVALID_STATUS2"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {Arrays.asList("ACTIVE", "INVALID_STATUS"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {Arrays.asList("ACTIVE".repeat(10)), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {Arrays.asList("PASSIVE$%^&*"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {Arrays.asList("active"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmailAddress")
    public static Object[][] invalidEmailAddress() {
        return new Object[][]{
                {null, ErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"invalid", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"},
                {"     ", ErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"abcgmail.com", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"},
                {"abc@gmail", ErrorMessage.MUST_BE_VALID, "emailAddress", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidIdData")
    public static Object[][] invalidIdData() {
        return new Object[][]{
                {"12345", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"abcdefghij", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27Z", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e2711", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"", ErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForCreateUser")
    public static Object[][] invalidCityDataForCreateUser() {
        return new Object[][]{
                {"City$Name", ErrorMessage.MUST_BE_VALID, "city", "String"},
                {"C".repeat(101), ErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City  ", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"  City", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "city", "String"},
                {null, ErrorMessage.MUST_NOT_BE_BLANK, "city", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidRoleName")
    public Object[][] invalidRoleName() {
        return new Object[][]{
                {"", ErrorMessage.SIZE_BETWEEN_2_255, "name", "String"},
                {"   Gönüllü", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "name", "String"},
                {"Gönüllü     ", ErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "name", "String"},
                {"123", ErrorMessage.MUST_BE_VALID, "name", "String"},
                {"?!'++", ErrorMessage.MUST_BE_VALID, "name", "String"},
                {"!Gönüllü", ErrorMessage.MUST_BE_VALID, "name", "String"},
                {"Gönüllü?", ErrorMessage.MUST_BE_VALID, "name", "String"},
                {"Gönüllü1", ErrorMessage.MUST_BE_VALID, "name", "String"},
                {"1Gönüllü", ErrorMessage.MUST_BE_VALID, "name", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForRoleList")
    public static Object[][] invalidStatusesDataForRoleList() {
        return new Object[][]{
                {Arrays.asList("INVALID_STATUS"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("INVALID_STATUS1", "INVALID_STATUS2"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("ACTIVE1"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("1ACTIVE"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("ACTIVE?"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("?ACTIVE"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("ACTIVE".repeat(10)), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("$%^&*"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("active"), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {Arrays.asList("      "), ErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPermissionIds")
    public static Object[][] invalidPermissionIds() {
        return new Object[][]{
                {Arrays.asList(""), ErrorMessage.MUST_NOT_BE_BLANK, "permissionIds[]", "permissionIds"},
                {Arrays.asList("        "), ErrorMessage.MUST_NOT_BE_BLANK, "permissionIds[]", "permissionIds"},
                {Arrays.asList("123"), ErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"},
                {Arrays.asList("$%^&*"), ErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"},
                {Arrays.asList("invalid"), ErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidRoleId")
    public Object[][] invalidRoleId() {
        return new Object[][]{
                {"123", ErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"invalid", ErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"       ", ErrorMessage.MUST_BE_VALID_UUID, "id", "String"},

        };
    }

}