package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {

    private String time;
    private String httpStatus;
    private boolean isSuccess;

}
