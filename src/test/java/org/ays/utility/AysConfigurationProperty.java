package org.ays.utility;

import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class AysConfigurationProperty {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("configuration.properties")) {
            PROPERTIES.load(input);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
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

    public static class SuperAdminUserOne {
        public static final String USERNAME = PROPERTIES.getProperty("institution.super_admin_user_one.username");
        public static final String PASSWORD = PROPERTIES.getProperty("institution.super_admin_user_one.password");
    }

    public static class InstitutionOne {
        public static final String ID = PROPERTIES.getProperty("institution_one.id");

        public static class AdminUserOne {
            public static final String USERNAME = PROPERTIES.getProperty("institution_one.admin_user_one.username");
            public static final String PASSWORD = PROPERTIES.getProperty("institution_one.admin_user_one.password");
        }
    }

}
