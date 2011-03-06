package fi.nylen.lzw;

/**
 * Thrown when the command line options given are not valid.
 */
public class IllegalOptionsException extends RuntimeException {
    public IllegalOptionsException(String message) {
        super(message);
    }
}
