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
public class RestErrorMessage {

    /**
     * 编码
     */
    private String code;
    /**
     * 信息
     */
    private String message;

    /**
     * 异常栈
     */
    private StackTraceElement[] cause;

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
                .append(">");
        return builder.toString();
    }
}
