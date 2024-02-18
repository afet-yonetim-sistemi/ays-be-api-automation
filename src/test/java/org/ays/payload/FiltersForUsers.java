package org.ays.payload;

import java.util.List;

public class FiltersForUsers {
    List<String> supportStatuses;
    List<String> statuses;
    PhoneNumber phoneNumber;
    private String firstName;
    private String lastName;

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

    public List<String> getSupportStatuses() {
        return supportStatuses;
    }

    public void setSupportStatuses(List<String> supportStatuses) {
        this.supportStatuses = supportStatuses;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
