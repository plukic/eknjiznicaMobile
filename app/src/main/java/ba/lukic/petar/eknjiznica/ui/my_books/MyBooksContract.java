package ba.lukic.petar.eknjiznica.ui.my_books;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;

public interface MyBooksContract {
    interface  Presenter extends BooksContract.Presenter<View> {
        void loadBooks(String bookName);
    }
    interface View extends BaseAsyncView<Presenter> {
        void displayMyBooks(List<ClientBookVM> clientBooks);
    }
}
