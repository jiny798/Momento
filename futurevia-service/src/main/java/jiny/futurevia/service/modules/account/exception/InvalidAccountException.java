package jiny.futurevia.service.modules.account.exception;

import jiny.futurevia.service.modules.exception.type.CommonException;

public class InvalidAccountException extends CommonException {
    public InvalidAccountException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
