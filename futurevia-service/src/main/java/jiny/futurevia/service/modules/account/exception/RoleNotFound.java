package jiny.futurevia.service.modules.account.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class RoleNotFound extends CommonException {

    private static final String MESSAGE = "권한(ROLE)이 존재하지 않습니다";

    public RoleNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
