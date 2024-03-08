package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysConfigurationProperty;

@Getter
@Setter
public class AdminCredentials {

    private String username;
    private String password;

    public static AdminCredentials generateIntsAdminCredentials() {
        AdminCredentials adminCredentials = new AdminCredentials();
        adminCredentials.setUsername(AysConfigurationProperty.InstitutionOne.AdminUserOne.USERNAME);
        adminCredentials.setPassword(AysConfigurationProperty.InstitutionOne.AdminUserOne.PASSWORD);
        return adminCredentials;
    }

}
