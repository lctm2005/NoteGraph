package com.licong.notemap.web.exception;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@NoArgsConstructor
@ControllerAdvice
public class DefaultRestErrorHandler extends RestErrorHandlerTemplate {

    @Autowired
    private RestErrorMessageWriter restErrorMessageWriter;

    @ExceptionHandler(value = {Exception.class})
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws Exception {
        restErrorMessageWriter.write(process(throwable, request), response, throwable);
    }

    @Override
    protected HttpStatus getHttpStatus(Throwable throwable, HttpServletRequest request) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    protected String getCode(Throwable throwable, HttpServletRequest request) {
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    @Override
    protected HttpHeaders getHttpHeaders(Throwable throwable, HttpServletRequest request) {
        return null;
    }

    @Override
    protected String getMessage(Throwable throwable) {
        return throwable.getLocalizedMessage();
    }

}