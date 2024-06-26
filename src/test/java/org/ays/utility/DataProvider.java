package org.ays.utility;

import lombok.experimental.UtilityClass;
import org.ays.payload.UserSupportStatus;

@UtilityClass
public class DataProvider {
    @org.testng.annotations.DataProvider(name = "positivePaginationData")
    public static Object[][] positivePaginationData() {
        return new Object[][]{
                {1, 10},
                {99999999, 1},
                {50, 50},
                {1000, 20}
        };
    }

    @org.testng.annotations.DataProvider(name = "negativePaginationData")
    public static Object[][] negativePaginationData() {
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

    @org.testng.annotations.DataProvider(name = "negativeSortData")
    public static Object[][] negativeSortData() {
        return new Object[][]{
                {null, "ASC", "must not be null"},
                {"createdAt", null, "must not be null"},
                {"", "ASC", "must be true"},
                {"invalid", "ASC", "must be true"},
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidNames")
    public static Object[][] invalidNames() {
        return new Object[][]{
                {"Noah", "Patricia1", "MUST BE VALID"},
                {".Noah", "Patricia1", "MUST BE VALID"},
                {" John", "Doe", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"Alice", "Johnson ", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"@#$%", "%^&*", "MUST BE VALID"},
                {"123", "Smith", "MUST BE VALID"},
                {"A", "B", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"A".repeat(256), "Doe", "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Jane", "B".repeat(256), "NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"}
        };
    }

    @org.testng.annotations.DataProvider
    public static Object[][] invalidPhoneNumberData() {
        return new Object[][]{
                {"90", "2105346789"},
                {"90", " 2367894534"},
                {"90", "005457568956"},
                {"+90", "5457568956"},
                {"90", "+5457568956"},

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
                {"firstName", "a", ErrorMessage.NAME_LENGTH, "String"},
                {"firstName", "a".repeat(101), ErrorMessage.NAME_LENGTH, "String"},
                {"firstName", "   ", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"firstName", "firstName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"firstName", "firstName4", ErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "a", ErrorMessage.NAME_LENGTH, "String"},
                {"lastName", "a".repeat(101), ErrorMessage.NAME_LENGTH, "String"},
                {"lastName", "   ", ErrorMessage.MUST_NOT_BE_BLANK, "String"},
                {"lastName", "lastName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"lastName", "lastName4", ErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "applicantName")
    public static Object[][] applicantName() {
        return new Object[][]{
                {"applicantFirstName", "a", ErrorMessage.NAME_LENGTH, "String"},
                {"applicantFirstName", "a".repeat(101), ErrorMessage.NAME_LENGTH, "String"},
                {"applicantFirstName", "firstName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "firstName4", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantLastName", "a", ErrorMessage.NAME_LENGTH, "String"},
                {"applicantLastName", "a".repeat(101), ErrorMessage.NAME_LENGTH, "String"},
                {"applicantLastName", "lastName$", ErrorMessage.MUST_BE_VALID, "String"},
                {"applicantFirstName", "lastName4", ErrorMessage.MUST_BE_VALID, "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "blankApplicantName")
    public static Object[][] blankApplicantName() {
        return new Object[][]{
                {"applicantFirstName", "", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED, "String"},
                {"applicantFirstName", "   ", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED, "String"},
                {"applicantFirstName", "", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED, "String"},
                {"applicantFirstName", "   ", ErrorMessage.ALL_APPLICANT_FIELDS_FILLED, "String"},
        };
    }


    @org.testng.annotations.DataProvider(name = "invalidSourceCityData")
    public static Object[][] invalidSourceCityData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"},
                {"City$Name", ErrorMessage.MUST_BE_VALID, "sourceCity", "String"},
                {"C".repeat(101), ErrorMessage.NAME_LENGTH, "sourceCity", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "sourceCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidSourceDistrictData")
    public static Object[][] invalidSourceDistrictData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "sourceDistrict", "String"},
                {"District$Name", ErrorMessage.MUST_BE_VALID, "sourceDistrict", "String"},
                {"D".repeat(101), ErrorMessage.NAME_LENGTH, "sourceDistrict", "String"},
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
                {"C".repeat(101), ErrorMessage.NAME_LENGTH, "targetCity", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "targetCity", "String"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidTargetDistrictData")
    public static Object[][] invalidTargetDistrictData() {
        return new Object[][]{
                {"", ErrorMessage.MUST_NOT_BE_BLANK, "targetDistrict", "String"},
                {"District$Name", ErrorMessage.MUST_BE_VALID, "targetDistrict", "String"},
                {"D".repeat(101), ErrorMessage.NAME_LENGTH, "targetDistrict", "String"},
                {"   ", ErrorMessage.MUST_NOT_BE_BLANK, "targetDistrict", "String"}
        };
    }

}