package edu.upc.campusnest.exception;

/** 409: se rompe una regla de negocio (ej. correo ya registrado). */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) { super(message); }
}
