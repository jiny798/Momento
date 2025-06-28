package jiny.futurevia.service.modules.account.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class DuplicateEmailException extends CommonException {

    private static final String MESSAGE = "중복된 이메일입니다.";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
