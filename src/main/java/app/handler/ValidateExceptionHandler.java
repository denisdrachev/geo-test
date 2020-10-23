package app.handler;

import app.exception.IncorrectInputException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ValidateExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<AwesomeException> handleMultipartException(Exception e) {
        return new ResponseEntity<>(new AwesomeException("Error. File not found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JsonMappingException.class, IncorrectInputException.class})
    protected ResponseEntity<AwesomeException> handleJsonMappingException(Exception e) {
        return new ResponseEntity<>(new AwesomeException("Incorrect input data"), HttpStatus.BAD_REQUEST);
    }

    @Data
    private static class AwesomeException {
        private final String message;
    }
}
