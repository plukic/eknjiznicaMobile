package ba.lukic.petar.eknjiznica.ui.books;

import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;

public interface BooksContract  {
    interface  Presenter<T> extends BasePresenter<T> {
        void onFavoriteToggle(BookOfferVM bookOfferVM);

        void onFavoriteToggle(BookOfferVM bookOfferVM, boolean displayNotification);

        boolean isInFavorite(BookOfferVM bookOfferVM);

        void onFavoriteToggle(ClientBookVM clientBookVM);

        void onFavoriteToggle(ClientBookVM clientBookVM , boolean displayNotification);

        boolean isInFavorite(ClientBookVM clientBookVM );
    }
}
