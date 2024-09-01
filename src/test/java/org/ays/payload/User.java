package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

import java.util.List;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private PhoneNumber phoneNumber;
    private String city;
    private List<String> roleIds;

    public static User generate() {
        User user = new User();
        user.setFirstName(AysRandomUtil.generateFirstName());
        user.setLastName(AysRandomUtil.generateLastName());
        user.setPhoneNumber(PhoneNumber.generateForTurkey());
        user.setEmailAddress(AysRandomUtil.generateEmailAddress());
        user.setCity(AysRandomUtil.generateRandomCity());
        user.setRoleIds(List.of("42fe288d-be87-4169-875e-e721a75cc833"));
        return user;
    }

    public static User generateUserWithARole(String roleId) {
        User user = new User();
        user.setFirstName(AysRandomUtil.generateFirstName());
        user.setLastName(AysRandomUtil.generateLastName());
        user.setPhoneNumber(PhoneNumber.generateForTurkey());
        user.setEmailAddress(AysRandomUtil.generateEmailAddress());
        user.setCity(AysRandomUtil.generateRandomCity());
        user.setRoleIds(List.of(roleId));
        return user;
    }

}
