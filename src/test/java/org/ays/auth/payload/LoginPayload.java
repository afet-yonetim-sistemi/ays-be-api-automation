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


    public static LoginPayload generateAsTestDisasterFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestDisasterFoundation.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestDisasterFoundation.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }

    public static LoginPayload generateAsTestDisasterFoundationUser() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestDisasterFoundation.User.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestDisasterFoundation.User.PASSWORD);
        loginPayload.setSourcePage(SourcePage.LANDING);
        return loginPayload;
    }


    public static LoginPayload generateAsTestVolunteerFoundationSuperAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestVolunteerFoundation.SuperAdmin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestVolunteerFoundation.SuperAdmin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }


    public static LoginPayload generateAsTestVolunteerFoundationAdmin() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestVolunteerFoundation.Admin.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestVolunteerFoundation.Admin.PASSWORD);
        loginPayload.setSourcePage(SourcePage.INSTITUTION);
        return loginPayload;
    }


    public static LoginPayload generateAsTestVolunteerFoundationUser() {
        LoginPayload loginPayload = new LoginPayload();
        loginPayload.setEmailAddress(AysConfigurationProperty.TestVolunteerFoundation.User.EMAIL_ADDRESS);
        loginPayload.setPassword(AysConfigurationProperty.TestVolunteerFoundation.User.PASSWORD);
        loginPayload.setSourcePage(SourcePage.LANDING);
        return loginPayload;
    }

}
