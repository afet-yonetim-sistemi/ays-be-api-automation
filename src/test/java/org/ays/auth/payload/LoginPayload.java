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
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.AfetYonetimSistemiAdmin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.AfetYonetimSistemiAdmin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsVolunteerFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.VolunteerFoundationAdmin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.VolunteerFoundationAdmin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsDisasterFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.DisasterFoundationAdmin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.DisasterFoundationAdmin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsTestFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestFoundationAdmin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestFoundationAdmin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsDisasterFoundationUser() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.DisasterFoundationUser.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.DisasterFoundationUser.PASSWORD);
        loginPayload.setSourcePage(SourcePage.LANDING);
        return loginPayload;
    }

}
