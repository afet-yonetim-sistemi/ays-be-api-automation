package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FiltersForAssignments {

    private List<String> statuses;
    private PhoneNumber phoneNumber;

}
