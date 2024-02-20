package org.ays.utility;

import org.ays.payload.Helper;

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
                {" John", "Doe", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"Alice", "Johnson ", "NAME MUST NOT START OR END WITH WHITESPACE"},
                {"@#$%", "%^&*", "MUST BE VALID"},
                {"123", "Smith", "MUST BE VALID"},
                {"Emma", "Br@wn", "MUST BE VALID"},
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
    public Object[][] countryCodeData() {
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
    public Object[][] lineNumberData() {
        return new Object[][]{
                {Helper.generateInvalidLineNumber()},
                {Helper.generateLineNumber() + "*"},
                {""}, {"          "},
                {null},
                {Helper.generateLineNumber() + "a"}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidLongitudeValues")
    public Object[][] invalidLongitudeValues() {
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
    public Object[][] invalidLatitudeValues() {
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
    public Object[][] invalidFirstAndLastNamesDataForAssignment() {
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
    public Object[][] invalidRejectReason() {
        return new Object[][]{
                {"A".repeat(39)},
                {"A".repeat(513)}
        };
    }

    @org.testng.annotations.DataProvider(name = "invalidDataForPostApplicationReasonField")
    public Object[][] invalidDataForPostApplicationReasonField() {
        return new Object[][]{
                {"reason less then forty", "size must be between 40 and 512", "reason", "String"},
                {null, "must not be blank", "reason", "String"},
                {"       ", "size must be between 40 and 512", "reason", "String"},
                {"A".repeat(513), "size must be between 40 and 512", "reason", "String"}
        };
    }

}
