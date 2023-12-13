package tests.institution.usermanagementservice;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Helper;
import payload.PhoneNumber;
import payload.User;
import payload.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUserTest {
    User userPayload;
    UserCredentials userCredentials;

    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod
    public void setupData() {
        userPayload=Helper.createUserPayload();
    }

    @Test()
    public void createAUser() {
        logger.info("Test case UMS_01 is running");
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
        response.then()
                .contentType("application/json")
                .body("response", notNullValue())
                .body("response.username", notNullValue())
                .body("response.password", notNullValue());
    }

    @Test(dataProvider = "userData")
    public void createUserNegative(String firstName, String lastName, String countryCode, String lineNumber) {
        User user=new User();
        PhoneNumber phoneNumber=new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        logger.info("Test case UMS_02-25 are running");
        Response response = InstitutionEndpoints.createAUser(userPayload);
        response.then()
                .contentType("application/json")
                .statusCode(400)
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false));
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][]{
                {"", "Mehmet", "90", "1334567811"},  // UMS_02
                {"4", "Mehmet", "90", "1334567811"},  // UMS_03
                {"44", "Mehmet", "90", "1334567811"},  // UMS_04
                {"A", "Mehmet", "90", "1334567811"},  // UMS_05
                {"A".repeat(256), "Mehmet", "90", "1334567811"},  // UMS_06
                {"Ahmet", "", "90", "1334567811"},  // UMS_09
                {"Ahmet", "1", "90", "1334567811"},  // UMS_10
                {"Ahmet", "12", "90", "1334567811"},  // UMS_11
                {"Ahmet", "M", "90", "1334567811"},  // UMS_12
                {"Ahmet", "M".repeat(256), "90", "1334567811"},  // UMS_13
                {"Ahmet", "Mehmet", "", "1334567811"},   // UMS_18
                {"Ahmet", "Mehmet", "90a", "1334567811"},  // UMS_21
                {"Ahmet", "Mehmet", " ", "1334567811"},  // UMS_22
                {"Ahmet", "Mehmet", "12345678", "1334567811"},  // UMS_20
                {"Ahmet", "Mehmet", "90", "13345678112345"},  // UMS_25
                {"Ahmet", "Mehmet", "90", ""},  // Empty line number
                {"Ahmet", "Mehmet", "90", "1334a567811"},  // UMS_24
        };
    }

}
