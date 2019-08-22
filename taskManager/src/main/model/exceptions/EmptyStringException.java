package model.exceptions;

public class EmptyStringException extends IllegalArgumentException {
    private String message;

    public EmptyStringException(){
    }

    public EmptyStringException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
