package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.SourcePage;
import org.ays.utility.AysConfigurationProperty;

@Getter
@Setter
public class SuperAdminCredentials {

    private String emailAddress;
    private String password;
    private SourcePage sourcePage;

    public static SuperAdminCredentials generate() {
        SuperAdminCredentials superAdminCredentials = new SuperAdminCredentials();
        superAdminCredentials.setEmailAddress(AysConfigurationProperty.SuperAdminUserOne.EMAIL_ADDRESS);
        superAdminCredentials.setPassword(AysConfigurationProperty.SuperAdminUserOne.PASSWORD);
        superAdminCredentials.setSourcePage(SourcePage.INSTITUTION);

        return superAdminCredentials;
    }

}
