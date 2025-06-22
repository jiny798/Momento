package jiny.futurevia.service.modules.order.exception;

import jiny.futurevia.service.modules.exception.type.CommonException;

public class OrderCancelException extends CommonException {
    private static final String MESSAGE = "권한(ROLE)이 존재하지 않습니다";

    public OrderCancelException() {
        super(MESSAGE);
    }

    public OrderCancelException(String message) {
        super(message);
    }


    @Override
    public int getStatusCode() {
        return 400;
    }
}
