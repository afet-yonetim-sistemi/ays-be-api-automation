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
    }

    public static class TestVolunteerFoundation {

        public static final String ID = PROPERTIES.getProperty("test_volunteer_foundation.id");

        public static class SuperAdmin {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_volunteer_foundation.super_admin.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("test_volunteer_foundation.super_admin.password");
        }

        public static class Admin {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_volunteer_foundation.admin.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("test_volunteer_foundation.admin.password");
        }

        public static class User {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_volunteer_foundation.user.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("test_volunteer_foundation.user.password");
        }
    }

    public static class TestDisasterFoundation {

        public static final String ID = PROPERTIES.getProperty("test_disaster_foundation.id");

        public static class Admin {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_disaster_foundation.admin.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("test_disaster_foundation.admin.password");
        }

        public static class User {
            public static final String EMAIL_ADDRESS = PROPERTIES.getProperty("test_disaster_foundation.user.email_address");
            public static final String PASSWORD = PROPERTIES.getProperty("test_disaster_foundation.user.password");
        }
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
