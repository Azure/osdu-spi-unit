package org.opengroup.osdu.unitservice.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppError {
    private int code;
    private String reason;
    private String message;
    // exclude debuggingInfo & originalException properties in response deserialization as they are not
    // required for swagger endpoint and Portal send weird multipart Content-Type in request
    @JsonIgnore
    private String debuggingInfo;
    @JsonIgnore
    private Exception originalException;
}
