package com.licong.notemap.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletResponse response, Exception e) throws Exception {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            e.printStackTrace(writer);
        } catch (IOException var10) {
            throw new RuntimeException(var10);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
