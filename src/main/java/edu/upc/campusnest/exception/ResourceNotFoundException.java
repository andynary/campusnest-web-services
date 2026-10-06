package edu.upc.campusnest.exception;

/** 404: el recurso no existe. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
