package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysConfigurationProperty;

@Getter
@Setter
public class AdminCredentials {

    private String emailAddress;
    private String password;
    private SourcePage sourcePage;

    public static AdminCredentials generate() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserOne.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return adminCredentials;
    }

    public static AdminCredentials generateForAdminTwo() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserTwo.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserTwo.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return adminCredentials;
    }

    public static AdminCredentials generateForTestAdmin() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.TestAdmin.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.TestAdmin.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return adminCredentials;
    }

}
