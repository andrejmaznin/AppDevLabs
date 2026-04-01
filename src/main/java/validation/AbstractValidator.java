package validation;

public abstract class AbstractValidator<T> implements Validator<T> {
    private Validator<T> next;

    @Override
    public void setNext(Validator<T> next) {
        this.next = next;
    }

    protected void callNext(T obj) {
        if (next != null) {
            next.validate(obj);
        }
    }
}
