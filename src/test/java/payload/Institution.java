package payload;

public class Institution {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private InstitutionDetail institution;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InstitutionDetail getInstitution() {
        return institution;
    }

    public void setInstitution(InstitutionDetail institution) {
        this.institution = institution;
    }
}
