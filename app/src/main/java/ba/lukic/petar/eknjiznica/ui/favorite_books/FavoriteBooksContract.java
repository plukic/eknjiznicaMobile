package ba.lukic.petar.eknjiznica.ui.favorite_books;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;

public interface FavoriteBooksContract {
    interface Presenter extends BooksContract.Presenter<View> {
        void loadFavoriteBooks();
    }
    interface View extends BaseAsyncView<Presenter> {
        void displayNoFavoriteBooks();
        void displayFavoriteBooks(List<BookOfferVM> favoriteBook);
    }
}
