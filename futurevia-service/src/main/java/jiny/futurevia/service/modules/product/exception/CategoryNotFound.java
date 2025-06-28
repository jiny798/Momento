package jiny.futurevia.service.modules.product.exception;

import jiny.futurevia.service.modules.common.exception.CommonException;

public class CategoryNotFound extends CommonException {

    private static final String MESSAGE = "존재하지 않는 카테고리입니다.";

    public CategoryNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
