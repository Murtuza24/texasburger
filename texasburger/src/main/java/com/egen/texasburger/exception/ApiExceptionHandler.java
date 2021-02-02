package com.egen.texasburger.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
@Log4j2
public class ApiExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
        log.info("handling bad requests.");
        ErrorResponse errors = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            ErrorMessage error = new ErrorMessage();
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage(violation.getMessage());
            errors.addError(error);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNotFound(CustomException e, WebRequest request) {
        log.info("handling not found.");
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes")
//    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<ErrorMessage> handleNoContent(CustomException e, WebRequest request) {
        log.info("handling No Content.");
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NO_CONTENT.value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception e, WebRequest request) {
        log.info("handling internal server error,");
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorMessage {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer code;

        private Date timestamp;
        private String message;
        private String description;

    }

    public static class ErrorResponse {

        private List<ErrorMessage> errors = new ArrayList<>();

        public List<ErrorMessage> getErrors() {
            return errors;
        }

        public void setErrors(List<ErrorMessage> errors) {
            this.errors = errors;
        }

        public void addError(ErrorMessage error) {
            this.errors.add(error);
        }

    }

//    public static class ErrorMessage {
//        private int statusCode;
//        private Date timestamp;
//        private String message;
//        private String description;
//
//        public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
//            this.statusCode = statusCode;
//            this.timestamp = timestamp;
//            this.message = message;
//            this.description = description;
//        }
//
//        public int getStatusCode() {
//            return statusCode;
//        }
//
//        public Date getTimestamp() {
//            return timestamp;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//    }
}