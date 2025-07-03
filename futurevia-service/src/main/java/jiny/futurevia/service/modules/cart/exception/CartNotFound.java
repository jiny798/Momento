package jiny.futurevia.service.modules.cart.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class CartNotFound extends CommonException {

    private static final String MESSAGE = "존재하지 않는 카트입니다.";

    public CartNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
