package ba.lukic.petar.eknjiznica.ui.book_details;

import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;

public interface BookDetailsContract {

    interface View extends BaseView<Presenter> {
        void toggleFavoriteIcon(boolean isFavorite);
        void displayBookAddedToBasket();

        void displayBookSendSuccessfully();

        void displayBookSendingError();

        void displaySendingBookToEmail(boolean isSending);
    }

    interface Presenter extends BooksContract.Presenter<View> {
        void addBookToBasket(BookOfferVM bookOfferVM);

        void downloadBook(BookOfferVM book);
    }
}
