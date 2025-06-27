package jiny.futurevia.service.modules.product.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class InvalidProductException extends CommonException {
    public InvalidProductException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
