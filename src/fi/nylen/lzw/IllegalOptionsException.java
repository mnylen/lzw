package fi.nylen.lzw;

class IllegalOptionsException extends RuntimeException {
    public IllegalOptionsException() {
        super();
    }
    
    public IllegalOptionsException(String message) {
        super(message);
    }
}
