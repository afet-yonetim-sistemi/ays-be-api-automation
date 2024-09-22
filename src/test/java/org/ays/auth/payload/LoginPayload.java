package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.SourcePage;
import org.ays.common.util.AysConfigurationProperty;

@Getter
@Setter
public class LoginPayload {

    private String emailAddress;
    private String password;
    private SourcePage sourcePage;

    public static LoginPayload generateAsAfetYonetimSistemiAdmin() {
        LoginPayload superAdminCredentials = new LoginPayload();
        superAdminCredentials.setEmailAddress(AysConfigurationProperty.AfetYonetimSistemiAdmin.EMAIL_ADDRESS);
        superAdminCredentials.setPassword(AysConfigurationProperty.AfetYonetimSistemiAdmin.PASSWORD);
        superAdminCredentials.setSourcePage(SourcePage.INSTITUTION);
        return superAdminCredentials;
    }

    public static LoginPayload generateAsVolunteerFoundationAdmin() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.VolunteerFoundationAdmin.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.VolunteerFoundationAdmin.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);
        return adminCredentials;
    }

    public static LoginPayload generateAsDisasterFoundationAdmin() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.DisasterFoundationAdmin.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.DisasterFoundationAdmin.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);
        return adminCredentials;
    }

    public static LoginPayload generateAsTestFoundationAdmin() {
        LoginPayload adminCredentials = new LoginPayload();
        adminCredentials.setEmailAddress(AysConfigurationProperty.TestFoundationAdmin.EMAIL_ADDRESS);
        adminCredentials.setPassword(AysConfigurationProperty.TestFoundationAdmin.PASSWORD);
        adminCredentials.setSourcePage(SourcePage.INSTITUTION);
        return adminCredentials;
    }

    public static LoginPayload generateAsDisasterFoundationUser() {
        LoginPayload userCredentials = new LoginPayload();
        userCredentials.setEmailAddress(AysConfigurationProperty.DisasterFoundationUser.EMAIL_ADDRESS);
        userCredentials.setPassword(AysConfigurationProperty.DisasterFoundationUser.PASSWORD);
        userCredentials.setSourcePage(SourcePage.LANDING);
        return userCredentials;
    }

}
