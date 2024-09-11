package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.SourcePage;
import org.ays.utility.AysConfigurationProperty;

@Getter
@Setter
public class LoginPayload {

    private String emailAddress;
    private String password;
    private SourcePage sourcePage;

    public static LoginPayload generateAsAdminUserOne() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserOne.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return adminCredentials;
    }

    public static LoginPayload generateAsAdminUserTwo() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.AdminUserTwo.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserTwo.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);
        return adminCredentials;
    }

    public static LoginPayload generateForTestAdmin() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.InstitutionOne.TestAdmin.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.TestAdmin.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return adminCredentials;
    }

}
