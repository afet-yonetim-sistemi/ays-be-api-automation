package payload;

public class User {
        private String createdUser;
        private String createdAt;
        private String updatedUser;
        private String updatedAt;
        private String id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private String status;
        private PhoneNumber phoneNumber;
        private String supportStatus;
        private Institution institution;

        public String getCreatedUser() {
            return createdUser;
        }

        public void setCreatedUser(String createdUser) {
            this.createdUser = createdUser;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedUser() {
            return updatedUser;
        }

        public void setUpdatedUser(String updatedUser) {
            this.updatedUser = updatedUser;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PhoneNumber getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSupportStatus() {
            return supportStatus;
        }

        public void setSupportStatus(String supportStatus) {
            this.supportStatus = supportStatus;
        }

        public Institution getInstitution() {
            return institution;
        }

        public void setInstitution(Institution institution) {
            this.institution = institution;
        }
    @Override
    public String toString() {
        return "User{" +
                "createdUser='" + createdUser + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedUser='" + updatedUser + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", supportStatus='" + supportStatus + '\'' +
                ", institution=" + institution +
                '}';
    }


}
