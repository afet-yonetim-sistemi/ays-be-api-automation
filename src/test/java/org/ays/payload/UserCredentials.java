package org.ays.payload;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.ays.endpoints.InstitutionEndpoints;

@Getter
@Setter
public class UserCredentials {

    private String username;
    private String password;

    public static UserCredentials generateCreate() {
        User user = User.generate();
        Response response = InstitutionEndpoints.createAUser(user);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials generateCreate(User userPayload) {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

}
