package com.confluent.feed.exception;

public class FeedValidationError implements ValidationError{
    private final String code;
    private final String msg;
    private final String recoverability;

    public FeedValidationError(String code, String msg, String recoverability) {
        this.code = code;
        this.msg = msg;
        this.recoverability = recoverability;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getRecoverability() {
        return recoverability;
    }

    @Override
    public String toString() {
        return "FeedValidationError : {" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", recoverability='" + recoverability + '\'' +
                '}';
    }
}
