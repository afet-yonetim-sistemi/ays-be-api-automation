package org.ays.common.util;

import lombok.experimental.UtilityClass;
import org.ays.common.model.enums.AysErrorMessage;

import java.util.List;

@UtilityClass
public class AysDataProvider {
    @org.testng.annotations.DataProvider(name = "positivePageableData")
    public static Object[][] positivePageableData() {
        return new Object[][]{
                {1, 10},
                {99999999, 10},
                {50, 10},
                {1000, 10}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPageableData")
    public static Object[][] invalidPageableData() {
        return new Object[][]{
                {-1, 10, AysErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "page", "int"},
                {100000000, 10, AysErrorMessage.MUST_BE_BETWEEN_1_AND_99999999, "page", "int"},
                {1, -10, AysErrorMessage.MUST_BE_10, "pageSize", "int"},
                {1, 1000000000, AysErrorMessage.MUST_BE_10, "pageSize", "int"},
                {-100, 100000000, AysErrorMessage.MUST_BE_10, "pageSize", "int"},
                {-5, -5, AysErrorMessage.MUST_BE_10, "pageSize", "int"},
                {100000000, 100000000, AysErrorMessage.MUST_BE_10, "pageSize", "int"},
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
                {null, AysErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"invalid", AysErrorMessage.MUST_BE_TRUE, "orderPropertyAccepted", null},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDirectionData")
    public static Object[][] invalidDirectionData() {
        return new Object[][]{
                {null, AysErrorMessage.MUST_NOT_BE_NULL, "direction", "AysSort.Direction"},
                {"", AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
                {"invalid", AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidNames")
    public static Object[][] invalidNames() {
        return new Object[][]{
                {"Noah", "Patricia1", AysErrorMessage.MUST_BE_VALID, "lastName", "String"},
                {".Noah", "Patricia", AysErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {" John", "Doe", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "firstName", "String"},
                {"Alice", "Johnson ", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "lastName", "String"},
                {"@#$%", "Johnson", AysErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {"Alice", "@#$%", AysErrorMessage.MUST_BE_VALID, "lastName", "String"},
                {"123", "Smith", AysErrorMessage.MUST_BE_VALID, "firstName", "String"},
                {"A".repeat(101), "Doe", AysErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"A", "Doe", AysErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"Jane", "B".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"},
                {"Jane", "B", AysErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"},
                {"", "Doe", AysErrorMessage.SIZE_BETWEEN_2_100, "firstName", "String"},
                {"Jane", "", AysErrorMessage.SIZE_BETWEEN_2_100, "lastName", "String"}
        };
    }

    @org.testng.annotations.DataProvider
    public static Object[][] invalidPhoneNumberData() {
        return new Object[][]{
                {"90", "2105346789", AysErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", " 2367894534", AysErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "005457568956", AysErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"+90", "5457568956", AysErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"90", "+5457568956", AysErrorMessage.MUST_BE_VALID, "phoneNumber", "AysPhoneNumberRequest"},
                {"", "5457568956", AysErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},
                {"90", "", AysErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {"  ", "5457568956", AysErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},
                {"90", "          ", AysErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {"90", null, AysErrorMessage.MUST_NOT_BE_BLANK, "lineNumber", "String"},
                {null, "5457568956", AysErrorMessage.MUST_NOT_BE_BLANK, "countryCode", "String"},

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
                {" firstName", "name must not start or end with whitespace"},
                {"25", "must be valid"},
                {"firstName ", "name must not start or end with whitespace"},
                {"firstName*", "must be valid"},
                {"F", "size must be between 2 and 100"},
                {"Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum has been the industrys standard dummy text ever since thes when an unknown printer took a galley of type and scrambled it to make a type specimen book It has su Letrasett", "size must be between 2 and 100"}

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

    @org.testng.annotations.DataProvider(name = "invalidFirstAndLastNamesDataForAdminRegistration")
    public Object[][] invalidFirstAndLastNamesDataForAdminRegistration() {
        return new Object[][]{
                {"", AysErrorMessage.MUST_NOT_BE_BLANK},
                {null, AysErrorMessage.MUST_NOT_BE_BLANK},
                {"       ", AysErrorMessage.MUST_NOT_BE_BLANK},
                {" firstName", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE},
                {"25", AysErrorMessage.MUST_BE_VALID},
                {"firstName ", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE},
                {"firstName*", AysErrorMessage.MUST_BE_VALID},
                {"F", AysErrorMessage.SIZE_BETWEEN_2_100},
                {".a", AysErrorMessage.MUST_BE_VALID},
                {"Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum has been the industrys standard dummy text ever since thes when an unknown printer took a galley of type and scrambled it to make a type specimen book It has su Letrasett", AysErrorMessage.SIZE_BETWEEN_2_100}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmailAddress")
    public Object[][] invalidEmailAddress() {
        return new Object[][]{
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"  ", AysErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"abc", AysErrorMessage.EMAIL_CONTAIN_EXACTLY_ONE_AT_CHARACTER, "emailAddress", "String"},
                {"abcgmail.com", AysErrorMessage.EMAIL_CONTAIN_EXACTLY_ONE_AT_CHARACTER, "emailAddress", "String"},
                {"abc@gmail", AysErrorMessage.EMAIL_IS_NOT_IN_A_VALID_FORMAT, "emailAddress", "String"},
                {null, AysErrorMessage.MUST_NOT_BE_BLANK, "emailAddress", "String"},
                {"  user@example.com", AysErrorMessage.EMAIL_MUST_NOT_START_OR_END_WITH_WHITESPACE, "emailAddress", "String"},
                {"user@-example.com", AysErrorMessage.DOMAIN_MUST_NOT_START_OR_END_WITH_A_HYPHEN, "emailAddress", "String"},
                {"-user@example.com", AysErrorMessage.EMAIL_LOCAL_PART_MUST_START_WITH_A_LETTER_OR_NUMBER, "emailAddress", "String"},
                {"user us@example.com", AysErrorMessage.EMAIL_CONTAINS_INVALID_SPECIAL_CHARACTERS, "emailAddress", "String"}
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

    @org.testng.annotations.DataProvider(name = "invalidName")
    public static Object[][] invalidName() {
        return new Object[][]{
                {"firstName", "", AysErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"firstName", "a", AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"firstName", "a".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"firstName", "   ", AysErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"firstName", "firstName$", AysErrorMessage.MUST_BE_VALID, "String"},
                {"firstName", "firstName4", AysErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "", AysErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "a", AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"lastName", "a".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"lastName", "   ", AysErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "lastName$", AysErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "lastName4", AysErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "applicantName")
    public static Object[][] applicantName() {
        return new Object[][]{
                {"applicantFirstName", "a", AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantFirstName", "a".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantFirstName", "firstName$", AysErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "firstName4", AysErrorMessage.MUST_BE_VALID, "String"},
                {"applicantLastName", "a", AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantLastName", "a".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "String"},
                {"applicantLastName", "lastName$", AysErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "lastName4", AysErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "blankApplicantName")
    public static Object[][] blankApplicantName() {
        return new Object[][]{
                {"applicantFirstName", "", AysErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantFirstName", "   ", AysErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantLastName", "", AysErrorMessage.ALL_APPLICANT_FIELDS_FILLED},
                {"applicantLastName", "   ", AysErrorMessage.ALL_APPLICANT_FIELDS_FILLED}
        };
    }


    @org.testng.annotations.DataProvider(name = "invalidSourceCityData")
    public static Object[][] invalidSourceCityData() {
        return new Object[][]{
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"},
                {"City$Name", AysErrorMessage.MUST_BE_VALID, "sourceCity", "String"},
                {"C".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"   ", AysErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidSourceDistrictData")
    public static Object[][] invalidSourceDistrictData() {
        return new Object[][]{
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "sourceDistrict", "String"},
                {"District$Name", AysErrorMessage.MUST_BE_VALID, "sourceDistrict", "String"},
                {"D".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"   ", AysErrorMessage.MUST_NOT_BE_BLANK, "sourceDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidAddressData")
    public static Object[][] invalidAddressData() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"Short", AysErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"A".repeat(251), AysErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"A", AysErrorMessage.SIZE_BETWEEN, "address", "String"},
                {"                      ", AysErrorMessage.MUST_NOT_BE_BLANK, "address", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidSeatingCountData")
    public static Object[][] invalidSeatingCountData() {
        return new Object[][]{
                {0, AysErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"},
                {-1, AysErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"},
                {1000, AysErrorMessage.SEATING_COUNT_BETWEEN, "seatingCount", "Integer"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetCityData")
    public static Object[][] invalidTargetCityData() {
        return new Object[][]{
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "targetCity", "String"},
                {"City$Name", AysErrorMessage.MUST_BE_VALID, "targetCity", "String"},
                {"C".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "targetCity", "String"},
                {"   ", AysErrorMessage.MUST_NOT_BE_BLANK, "targetCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidEmailAddressForUsersList")
    public static Object[][] invalidEmailAddressDataForUsersList() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_255, "emailAddress", "String"},
                {"E", AysErrorMessage.SIZE_BETWEEN_2_255, "emailAddress", "String"},
                {"E".repeat(256), AysErrorMessage.SIZE_BETWEEN_2_255, "emailAddress", "String"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForUsersList")
    public static Object[][] invalidCityDataForUsersList() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City$Name", AysErrorMessage.MUST_BE_VALID, "city", "String"},
                {"C".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City  ", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"  City", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetDistrictDataForFilteringEvacuationApplications")
    public static Object[][] invalidTargetDistrictDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"District$Name", AysErrorMessage.CONTAINS_INVALID_CHARACTERS, "targetDistrict", "String"},
                {"D".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"D", AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetDistrictData")
    public static Object[][] invalidTargetDistrictData() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"District$Name", AysErrorMessage.MUST_BE_VALID, "targetDistrict", "String"},
                {"D".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"D", AysErrorMessage.SIZE_BETWEEN_2_100, "targetDistrict", "String"},
                {"   ", AysErrorMessage.MUST_NOT_BE_BLANK, "targetDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForFilteringEvacuationApplications")
    public static Object[][] invalidCityDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"City$Name", AysErrorMessage.CONTAINS_INVALID_CHARACTERS, "sourceCity", "String"},
                {"C".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"C", AysErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"},
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "sourceCity", "String"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetCityDataForFilteringEvacuationApplications")
    public static Object[][] invalidTargetCityDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"Target$City", AysErrorMessage.CONTAINS_INVALID_CHARACTERS, "targetCity", "String"},
                {"T".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "targetCity", "String"},
                {"T", AysErrorMessage.SIZE_BETWEEN_2_100, "targetCity", "String"},
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "targetCity", "String"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDistrictDataForFilteringEvacuationApplications")
    public static Object[][] invalidDistrictDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"District$Name", AysErrorMessage.CONTAINS_INVALID_CHARACTERS, "sourceDistrict", "String"},
                {"D".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"},
                {"D", AysErrorMessage.SIZE_BETWEEN_2_100, "sourceDistrict", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForFilteringEvacuationApplications")
    public static Object[][] invalidStatusesDataForFilteringEvacuationApplications() {
        return new Object[][]{
                {List.of("INVALID_STATUS"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {List.of("INVALID_STATUS1", "INVALID_STATUS2"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {List.of("PENDING", "INVALID_STATUS"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {List.of("PENDING".repeat(10)), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {List.of("PENDING$%^&*"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"},
                {List.of("pending"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "EmergencyEvacuationApplicationStatus"}
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
                {null, "ASC", AysErrorMessage.MUST_NOT_BE_BLANK},
                {"createdAt", null, AysErrorMessage.MUST_NOT_BE_NULL},
                {"", "ASC", AysErrorMessage.MUST_NOT_BE_BLANK},
                {"invalid", "DESC", AysErrorMessage.MUST_BE_TRUE},
                {"createdAt", "invalid", AysErrorMessage.MUST_BE_ACCEPTED_VALUE},
                {"createdAt", "", AysErrorMessage.MUST_BE_ACCEPTED_VALUE}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidOrderData")
    public static Object[][] invalidOrderData() {
        return new Object[][]{
                {null, "ASC", AysErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"createdAt", null, AysErrorMessage.MUST_NOT_BE_NULL, "direction", "AysSort.Direction"},
                {"", "ASC", AysErrorMessage.MUST_NOT_BE_BLANK, "property", "String"},
                {"createdAt", "invalid", AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"},
                {"createdAt", "", AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "direction", "AysSort.Direction"}
        };
    }

    @org.testng.annotations.DataProvider(name = "negativeReferenceNumberData")
    public static Object[][] negativeReferenceNumberData() {
        return new Object[][]{
                {"referenceNumber", "12345678901", "String", AysErrorMessage.SIZE_BETWEEN_1_10},
                {"referenceNumber", "abcdefghjkl", "String", AysErrorMessage.SIZE_BETWEEN_1_10},
                {"referenceNumber", "1234%^*(*)@", "String", AysErrorMessage.SIZE_BETWEEN_1_10}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForUsersList")
    public static Object[][] invalidStatusesDataForUsersList() {
        return new Object[][]{
                {List.of("INVALID_STATUS"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {List.of("INVALID_STATUS1", "INVALID_STATUS2"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {List.of("ACTIVE", "INVALID_STATUS"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {List.of("ACTIVE".repeat(10)), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {List.of("PASSIVE$%^&*"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"},
                {List.of("active"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysUserStatus"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidRoleIdListData")
    public static Object[][] invalidRoleIdListData() {
        return new Object[][]{
                {"12345", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"abcdefghij", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27Z", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e2711", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"},
                {"", AysErrorMessage.MUST_BE_VALID_UUID, "roleIds[]", "roleIds"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidCityDataForCreateUser")
    public static Object[][] invalidCityDataForCreateUser() {
        return new Object[][]{
                {"City$Name", AysErrorMessage.MUST_BE_VALID, "city", "String"},
                {"C".repeat(101), AysErrorMessage.SIZE_BETWEEN_2_100, "city", "String"},
                {"City  ", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"  City", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "city", "String"},
                {"", AysErrorMessage.MUST_NOT_BE_BLANK, "city", "String"},
                {null, AysErrorMessage.MUST_NOT_BE_BLANK, "city", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidRoleName")
    public Object[][] invalidRoleName() {
        return new Object[][]{
                {"", AysErrorMessage.SIZE_BETWEEN_2_255, "name", "String"},
                {"   Gönüllü", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "name", "String"},
                {"Gönüllü     ", AysErrorMessage.NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE, "name", "String"},
                {"123", AysErrorMessage.MUST_BE_VALID, "name", "String"},
                {"?!'++", AysErrorMessage.MUST_BE_VALID, "name", "String"},
                {"!Gönüllü", AysErrorMessage.MUST_BE_VALID, "name", "String"},
                {"Gönüllü?", AysErrorMessage.MUST_BE_VALID, "name", "String"},
                {"Gönüllü1", AysErrorMessage.MUST_BE_VALID, "name", "String"},
                {"1Gönüllü", AysErrorMessage.MUST_BE_VALID, "name", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidStatusesDataForRoleList")
    public static Object[][] invalidStatusesDataForRoleList() {
        return new Object[][]{
                {List.of("INVALID_STATUS"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("INVALID_STATUS1", "INVALID_STATUS2"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("ACTIVE1"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("1ACTIVE"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("ACTIVE?"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("?ACTIVE"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("ACTIVE".repeat(10)), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("$%^&*"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("active"), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"},
                {List.of("      "), AysErrorMessage.MUST_BE_ACCEPTED_VALUE, "statuses", "AysRoleStatus"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidPermissionIds")
    public static Object[][] invalidPermissionIds() {
        return new Object[][]{
                {List.of(""), AysErrorMessage.MUST_NOT_BE_BLANK, "permissionIds[]", "permissionIds"},
                {List.of("        "), AysErrorMessage.MUST_NOT_BE_BLANK, "permissionIds[]", "permissionIds"},
                {List.of("123"), AysErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"},
                {List.of("$%^&*"), AysErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"},
                {List.of("invalid"), AysErrorMessage.MUST_BE_VALID_UUID, "permissionIds[]", "permissionIds"},
                {null, AysErrorMessage.MUST_NOT_BE_EMPTY, "permissionIds", "Set"}

        };
    }

    @org.testng.annotations.DataProvider(name = "invalidIdFormat")
    public Object[][] invalidIdFormat() {
        return new Object[][]{
                {"123", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"invalid", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"       ", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27Z", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e27", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804e2711", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
                {"02fe9d68-70b7-4b53-abb4-3e18e804", AysErrorMessage.MUST_BE_VALID_UUID, "id", "String"},
        };
    }

}