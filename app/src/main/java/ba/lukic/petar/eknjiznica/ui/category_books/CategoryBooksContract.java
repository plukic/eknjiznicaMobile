package ba.lukic.petar.eknjiznica.ui.category_books;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;

public interface CategoryBooksContract {
    interface  Presenter extends BooksContract.Presenter<View> {
        void loadBooks(int categoryId,String bookName);
    }
    interface View extends BaseAsyncView<Presenter> {
        void displayBooks(List<BookOfferVM> bookOfferVMS);
    }
}
