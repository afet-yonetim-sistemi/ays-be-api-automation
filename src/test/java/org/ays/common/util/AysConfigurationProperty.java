package org.ays.common.util;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serial;
import java.util.Properties;

@UtilityClass
public class AysConfigurationProperty {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("configuration.properties")) {

            PROPERTIES.load(input);

            if (PROPERTIES.isEmpty()) {
                throw new AysConfigurationException("Configuration properties are empty!");
            }

        } catch (IOException exception) {
            throw new AysConfigurationException(exception);
        }
    }

    public static class Api {
        public static final String URL = PROPERTIES.getProperty("api.url");
    }

    public static class Database {
        public static final String URL = PROPERTIES.getProperty("database.url");
        public static final String USERNAME = PROPERTIES.getProperty("database.username");
        public static final String PASSWORD = PROPERTIES.getProperty("database.password");
        public static final String VOLUNTEER_FOUNDATION_ID = PROPERTIES.getProperty("volunteer_foundation_id");
        public static final String AFET_YONETIM_SISTEMI_ID = PROPERTIES.getProperty("afet_yonetim_sistemi_id");
        public static final String DISASTER_FOUNDATION_ID = PROPERTIES.getProperty("disaster_foundation_id");
        public static final String TEST_FOUNDATION_ID = PROPERTIES.getProperty("test_foundation_id");
    }

    public static class SuperAdminUserOne {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("afet_yonetim_sistemi_admin.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("afet_yonetim_sistemi_admin.password");
    }

    public static class AdminUserOne {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("volunteer_foundation_admin.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("volunteer_foundation_admin.password");
    }

    public static class AdminUserTwo {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("disaster_foundation_admin.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("disaster_foundation_admin.password");
    }

    public static class TestAdmin {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_foundation_admin.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("test_foundation_admin.password");
    }


    public static class LandingUserOne {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("landing_page_user_martha.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("landing_page_user_martha.password");
    }

    private static class AysConfigurationException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = -3108411992530107699L;

        public AysConfigurationException(String message) {
            super(message);
        }

        public AysConfigurationException(Throwable cause) {
            super(cause);
        }

    }

}
