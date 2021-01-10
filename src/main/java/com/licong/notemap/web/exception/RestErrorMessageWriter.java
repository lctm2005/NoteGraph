package com.licong.notemap.web.exception;

import com.licong.notemap.util.JsonUtils;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class RestErrorMessageWriter {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 输出错误信息
     *
     * @param entity
     * @param response
     * @param throwable
     */
    public void write(ResponseEntity<RestErrorMessage> entity, HttpServletResponse response, Throwable throwable) {
        try {
            HttpHeaders headers = entity.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.toSingleValueMap().entrySet()) {
                    response.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response.setStatus(entity.getStatusCode().value());
            response.setContentType(CONTENT_TYPE);
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtils.toJson(entity.getBody()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
