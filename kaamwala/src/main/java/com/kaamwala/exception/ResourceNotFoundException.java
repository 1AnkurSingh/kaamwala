package com.kaamwala.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){
        super("Resource Not Found Exception !!");
    }
    
    public ResourceNotFoundException(String message){
        super(message);
    }
    
    // Constructor for formatted messages (resourceName, fieldName, fieldValue)
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
}
