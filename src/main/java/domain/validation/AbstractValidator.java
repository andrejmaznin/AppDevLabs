package domain.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractValidator<T> implements Validator<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Validator<T> next;

    @Override
    public void setNext(Validator<T> next) {
        this.next = next;
    }

    protected void callNext(T obj) {
        if (next != null) {
            logger.trace("Валидация {}: переход к {}", getClass().getSimpleName(), next.getClass().getSimpleName());
            next.validate(obj);
        } else {
            logger.debug("Цепочка валидации успешно завершена на {}", getClass().getSimpleName());
        }
    }
}
