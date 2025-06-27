package jiny.futurevia.service.modules.account.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class UserNotFound extends CommonException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
