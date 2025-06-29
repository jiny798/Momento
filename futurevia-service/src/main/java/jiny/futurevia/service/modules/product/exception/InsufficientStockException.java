package jiny.futurevia.service.modules.product.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class InsufficientStockException extends CommonException {

    private static final String MESSAGE = "품절입니다";

    public InsufficientStockException() {
        super(MESSAGE);
    }

    public InsufficientStockException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
