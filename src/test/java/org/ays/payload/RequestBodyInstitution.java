package org.ays.payload;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.ays.endpoints.InstitutionEndpoints;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RequestBodyInstitution {

    private Pagination pagination;
    private List<Sort> sort;
    private Filter filter;

    public static String generateRegistrationApplicationID() {
        RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
        requestBodyInstitution.setPagination(Pagination.setPagination(1, 10));
        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        List<Map<String, Object>> content = response.jsonPath().getList("response.content");
        if (content != null && !content.isEmpty()) {
            String applicationId = (String) content.get(0).get("id");
            if (applicationId != null) {
                return applicationId;
            } else {
                throw new RuntimeException("Application ID is null");
            }
        } else {
            throw new RuntimeException("No registration applications found");
        }
    }

}
