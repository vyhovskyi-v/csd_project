package com.github.vyhovskyi.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
    public ServerException(String message, Throwable cause) {super(message, cause);}
    public ServerException(Throwable cause) {super(cause);}
}
