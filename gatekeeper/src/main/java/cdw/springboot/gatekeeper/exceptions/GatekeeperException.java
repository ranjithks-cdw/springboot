package cdw.springboot.gatekeeper.exceptions;

import org.springframework.http.HttpStatus;

public class GatekeeperException extends RuntimeException {
    private final HttpStatus httpStatus;

    public GatekeeperException(String message) {
        this(message,HttpStatus.BAD_REQUEST);
    }

    public GatekeeperException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
