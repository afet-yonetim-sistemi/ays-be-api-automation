package utility;

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
                {null, "ASC","must not be null"},
                {"createdAt", null,"must not be null"},
                {"", "ASC","must be true"},
                {"invalid", "ASC","must be true"},
        };
    }
    @org.testng.annotations.DataProvider(name = "invalidNames")
    public static Object[][] invalidNames() {
        return new Object[][] {
                {"Noah", "Patricia1","MUST BE VALID"},
                {" John", "Doe","NAME MUST NOT START OR END WITH WHITESPACE"},
                {"Alice", "Johnson ","NAME MUST NOT START OR END WITH WHITESPACE"},
                {"@#$%", "%^&*","MUST BE VALID"},
                {"123", "Smith","MUST BE VALID"},
                {"Emma", "Br@wn","MUST BE VALID"},
                {"A", "B","NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"A".repeat(256), "Doe","NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"},
                {"Jane", "B".repeat(256),"NAME MUST BE BETWEEN 2 AND 255 CHARACTERS LONG"}
        };
    }
    @org.testng.annotations.DataProvider
    public static Object[][] invalidPhoneNumberData() {
        return new Object[][]{
                {"90","2105346789"},
                {"90"," 2367894534"},
                {"90","005457568956"},
                {"+90","5457568956"},
                {"90","+5457568956"},

        };
    }
}
