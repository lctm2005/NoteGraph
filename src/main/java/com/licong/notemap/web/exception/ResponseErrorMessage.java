package com.licong.notemap.web.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vime
 * @since 0.9.6
 */
@Data
@NoArgsConstructor
public class ResponseErrorMessage {

    private String code;
    private String message;
    private String detail;
    private Date serverTime = new Date();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append("code:")
                .append(getCode())
                .append(", message:")
                .append(getMessage())
                .append(", server_time:")
                .append(serverTime)
                .append(", detail:")
                .append(getDetail())
                .append(">");
        return builder.toString();
    }
}
