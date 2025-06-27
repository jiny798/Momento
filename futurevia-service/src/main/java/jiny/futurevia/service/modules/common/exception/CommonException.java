package jiny.futurevia.service.modules.common.exception;

public abstract class CommonException extends RuntimeException {
    public CommonException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
