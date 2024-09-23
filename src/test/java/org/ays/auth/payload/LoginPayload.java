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
        loginPayload.setEmailAddress(AysConfigurationProperty.AfetYonetimSistemi.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.AfetYonetimSistemi.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsVolunteerFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.VolunteerFoundation.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.VolunteerFoundation.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsDisasterFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.DisasterFoundation.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.DisasterFoundation.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsTestFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestFoundation.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestFoundation.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsDisasterFoundationUser() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.DisasterFoundation.User.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.DisasterFoundation.User.PASSWORD);
        loginPayload.setSourcePage(SourcePage.LANDING);
        return loginPayload;
    }

}
