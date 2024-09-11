package org.ays.utility;

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
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("institution.super_admin_user_one.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("institution.super_admin_user_one.password");
    }

    public static class InstitutionOne {
        public static final String ID = PROPERTIES.getProperty("institution_one.id");

        public static class AdminUserOne {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("institution_one.admin_user_one.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("institution_one.admin_user_one.password");
        }

        public static class AdminUserTwo {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("institution_one.admin_user_two.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("institution_one.admin_user_two.password");
        }

        public static class TestAdmin {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("institution.test_admin.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("institution.test_admin.password");
        }

    }

    public static class LandingUserOne {
        public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("landing.user_one.email_address");
        public static final String PASSWORD = PROPERTIES.getProperty("landing.user_one.password");
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
