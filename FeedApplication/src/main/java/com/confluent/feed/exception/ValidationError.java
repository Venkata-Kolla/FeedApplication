package com.confluent.feed.exception;

public interface ValidationError {

    String getCode();

    String getMsg();

    String getRecoverability();

    default String toStringDefault() {
        return "ValidationError{code=" + getCode() + ",msg=" + getMsg() + "}";
    }
}
