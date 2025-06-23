package jiny.futurevia.service.modules.product.exception;

import jiny.futurevia.service.modules.exception.type.CommonException;

public class InvalidProductException extends CommonException {
    public InvalidProductException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
