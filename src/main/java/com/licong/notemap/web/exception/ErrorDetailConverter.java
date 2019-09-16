package com.licong.notemap.web.exception;

public interface ErrorDetailConverter {

    String getDetail(String detail, Throwable throwable);
}
