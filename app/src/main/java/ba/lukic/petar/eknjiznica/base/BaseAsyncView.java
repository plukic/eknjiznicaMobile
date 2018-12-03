package ba.lukic.petar.eknjiznica.base;

public interface BaseAsyncView<T>  extends BaseView<T>{

    void displayUnexpectedError();
    void displayLoading(boolean isLoading);
}
