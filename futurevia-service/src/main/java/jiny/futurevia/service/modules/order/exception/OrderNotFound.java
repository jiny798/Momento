package jiny.futurevia.service.modules.order.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class OrderNotFound extends CommonException {

    private static final String MESSAGE = "존재하지 않는 주문입니다.";

    public OrderNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
