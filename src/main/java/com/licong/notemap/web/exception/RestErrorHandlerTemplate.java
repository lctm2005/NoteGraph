package com.licong.notemap.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * Restful异常处理模板类
 */
public abstract class RestErrorHandlerTemplate {

    public ResponseEntity<RestErrorMessage> process(Throwable throwable, HttpServletRequest request) {
        Assert.notNull(throwable, "cause");
        Assert.notNull(request, "request");

        RestErrorMessage restErrorMessage = new RestErrorMessage();
        restErrorMessage.setMessage(getMessage(throwable));
        restErrorMessage.setCode(getCode(throwable, request));
        restErrorMessage.setCause(throwable.getStackTrace());

        HttpHeaders httpHandlers = getHttpHeaders(throwable, request);
        HttpStatus httpStatus = getHttpStatus(throwable, request);

        return new ResponseEntity<>(restErrorMessage, httpHandlers, httpStatus);
    }

    protected abstract HttpStatus getHttpStatus(Throwable throwable, HttpServletRequest request);

    protected abstract HttpHeaders getHttpHeaders(Throwable throwable, HttpServletRequest request);

    protected abstract String getCode(Throwable throwable, HttpServletRequest request);

    protected abstract String getMessage(Throwable throwable);
}
