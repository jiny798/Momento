package jiny.futurevia.service.modules.exception.type;

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
