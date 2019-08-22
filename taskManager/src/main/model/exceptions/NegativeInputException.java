package model.exceptions;

public class NegativeInputException extends IllegalArgumentException {
    private String message;

    public NegativeInputException(){}

    public NegativeInputException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
