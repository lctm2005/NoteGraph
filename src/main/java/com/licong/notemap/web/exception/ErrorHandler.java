package com.licong.notemap.web.exception;

import com.licong.notemap.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    private static final String MESSAGE = "INTERNAL_SERVER_ERROR";
    private static final String CODE = "500";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String NEWLINE = "\t\n";


    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws Exception {
        ResponseEntity<ErrorMessage> entity = process(throwable, request);
        try {
            log.error(entity.getBody().toString(), throwable);

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

    public ResponseEntity<ErrorMessage> process(Throwable throwable, HttpServletRequest request) {
        Assert.notNull(throwable, "throwable");
        Assert.notNull(request, "request");

        ErrorMessage errorMessage = getBody(throwable, request);
        HttpStatus httpStatus = getHttpStatus(throwable, request);
        return new ResponseEntity<>(errorMessage, httpStatus);
    }

    protected ErrorMessage getBody(Throwable throwable, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(MESSAGE);
        errorMessage.setDetail(appendStackTrace(null, throwable));
        errorMessage.setCode(getCode(throwable, request));
        return errorMessage;
    }


    private HttpStatus getHttpStatus(Throwable throwable, HttpServletRequest request) {
        //TODO 从自定义异常中获取
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String getCode(Throwable throwable, HttpServletRequest request) {
        //TODO 从自定义异常中获取
        return CODE;
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    private String appendStackTrace(String detail, Throwable throwable) {
        if (throwable != null) {
            if (detail == null) {
                detail = "";
            } else {
                detail += NEWLINE;
            }
            detail += "Stack trace:" + NEWLINE + getStackTrace(throwable);
        }
        return detail;
    }

}
