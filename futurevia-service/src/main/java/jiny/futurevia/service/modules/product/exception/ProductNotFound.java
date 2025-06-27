package jiny.futurevia.service.modules.product.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class ProductNotFound extends CommonException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public ProductNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
