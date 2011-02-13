package fi.nylen.lzw;

public class IllegalOptionsException extends RuntimeException {
    public IllegalOptionsException() {
        super();
    }
    
    public IllegalOptionsException(String message) {
        super(message);
    }
}
