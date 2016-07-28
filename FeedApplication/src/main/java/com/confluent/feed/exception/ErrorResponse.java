package com.confluent.feed.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "errorResponse")
public class ErrorResponse {

    private List<Error> errors = new ArrayList<>();


    public ErrorResponse() {
    }

    public ErrorResponse(Error error) {
        add(error);
    }

    public ErrorResponse(ValidationError validationError) {
        add(new Error(validationError));
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void add(Error error) {
        errors.add(error);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "errors=" + errors + '}';
    }
}
