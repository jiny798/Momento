package jiny.futurevia.service.modules.account.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class InvalidAccountException extends CommonException {
    public InvalidAccountException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
