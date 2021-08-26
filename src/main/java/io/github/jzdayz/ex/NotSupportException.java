package io.github.jzdayz.ex;

public class NotSupportException extends RuntimeException{

    public NotSupportException() {
        super();
    }

    public NotSupportException(String message) {
        super(message);
    }
}
