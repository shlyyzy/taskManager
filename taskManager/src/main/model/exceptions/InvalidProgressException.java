package model.exceptions;

public class InvalidProgressException extends IllegalArgumentException {
    private String message;

    public InvalidProgressException(){}

    public InvalidProgressException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
