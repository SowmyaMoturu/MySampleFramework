package exceptions;

public class InvalidPropertyException extends RuntimeException{

    public InvalidPropertyException(String key) {
        super("Invalid Property: " + key);
    }
}
