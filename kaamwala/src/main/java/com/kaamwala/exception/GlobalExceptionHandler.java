package com.kaamwala.exception;


import com.kaamwala.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)

    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        logger.info("Exception handler invoke");

        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.NOT_FOUND);

    }

    // Method Argument not valid

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotValidException(MethodArgumentNotValidException ex) {

        logger.info("MethodArgumentNotValidException invoke");

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        System.out.println(allErrors);

        Map<String, Object> response = new HashMap<>();

        allErrors.stream()
                .forEach(objectError -> {
                    String message = objectError.getDefaultMessage();

                    String field = ((FieldError) objectError).getField();
                    response.put(field, message);

                });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handel Bad Api exception
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handelBadApiRequest(BadApiRequest ex) {
        logger.info("Bad Api Request");

        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.BAD_REQUEST);

    }
    //HttpRequestMethodNotSupportedException
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponseMessage> handleMethodNotSupported(HttpMediaTypeNotSupportedException ex){
        logger.warn("Unsupported Http Method");
        return  new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .message(ex.getMessage())
                        .success(false)
                        .status(HttpStatus.METHOD_NOT_ALLOWED)
                        .build(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }


//HttpMessageNotReadableException // new
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseMessage> handleMessageNotReadable(Exception ex){
        logger.info("MessageNotReadable exception occurred: ");
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.BAD_REQUEST);
    }

    //Generic Exception (Catch-All)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseMessage> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred: {}", ex.getMessage());
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .success(false)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
