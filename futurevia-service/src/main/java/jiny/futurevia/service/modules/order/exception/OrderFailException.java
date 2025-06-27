package jiny.futurevia.service.modules.order.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class OrderFailException extends CommonException {

    private static final String MESSAGE = "주문 실패";

    public OrderFailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
