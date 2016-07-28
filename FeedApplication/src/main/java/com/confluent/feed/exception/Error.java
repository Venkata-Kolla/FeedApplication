package com.confluent.feed.exception;


public class Error {
    private String field;
    private String code;
    private String message;
    private String recoverability;

    public Error() {
    }

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Error(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }

    public Error(String field, String code, String message, String recoverability) {
        this.field = field;
        this.code = code;
        this.message = message;
        this.recoverability = recoverability;
    }

    public Error(ValidationError error) {
        this.code = error.getCode();
        this.message = error.getMsg();
        this.recoverability =  error.getRecoverability();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecoverability() {
        return this.recoverability;
    }

    public void setRecoverability(String recoverability) {
        this.recoverability = recoverability;
    }

    @Override
    public String toString() {
        return "Error{" + "field='" + field + '\'' + ", code='" + code + '\'' + ", message='"
                + message + '\'' + ", recoverability='" + recoverability + '\'' + '}';
    }
}
