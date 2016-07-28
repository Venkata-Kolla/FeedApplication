package com.confluent.feed.exception;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;


import java.util.List;

@ControllerAdvice
public class ControllerErrorHandler {

    private Logger logger = LoggerFactory.getLogger(ControllerErrorHandler.class);

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Order(value = 1)
    public ErrorResponse validationErrors(MethodArgumentNotValidException manv) {
        BindingResult bindingResult = manv.getBindingResult();
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        return processFieldErrors(fieldErrorList);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @Order(value = 2)
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException hmnrExcep) {
        Error error = new Error(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid payload."+hmnrExcep.getMessage());
        error.setRecoverability("Recoverable");
        return new ErrorResponse(error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({ ConfluentBusinessRuntimeException.class})
    @Order(value = 3)
    public ErrorResponse remoteClientException(ConfluentBusinessRuntimeException clientError) {
        ValidationError validationError = clientError.getValidationError();
        Error error = new Error(validationError.getCode(), validationError.getMsg());
        setRecoverability(validationError, error);
        return new ErrorResponse(error);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler({ ConstraintViolationException.class, DataIntegrityViolationException.class, })
    @Order(value = 4)
    public ErrorResponse processConstraintViolationException(DataIntegrityViolationException ex) {
        logger.error("SQL ConstraintViolationException error occurred", ex);
        String errorMessage = ex.getMessage();
        Error error = new Error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "SQL Execution Error", errorMessage, "Un Recoverable");
        return new ErrorResponse(error);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler({ Exception.class})
    @Order(value = 5)
    public ErrorResponse processRuntimeException(Exception ex) {
        logger.error("Unknown error occoured ", ex);
        String errorMessage = ex.getMessage();
        if(StringUtils.isBlank(errorMessage)){
            errorMessage = "Error occurred while processing the request.";
        }
        Error error = new Error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Internal Error", errorMessage, "Recoverable");
        return new ErrorResponse(error);
    }

    private ErrorResponse processFieldErrors(List<FieldError> fieldErrors) {
        ErrorResponse validationErrorModel = new ErrorResponse();
        for (FieldError fieldError : fieldErrors) {
            validationErrorModel.add(new Error(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()));
        }
        return validationErrorModel;
    }
    
    private void setRecoverability(ValidationError validationError, Error error) {
        String recoverable = "Not Recoverable";
        error.setRecoverability(recoverable);
    }
}
