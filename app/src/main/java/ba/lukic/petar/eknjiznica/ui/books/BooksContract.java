package ba.lukic.petar.eknjiznica.ui.books;

import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public interface BooksContract  {
    interface  Presenter<T> extends BasePresenter<T> {
        void onFavoriteToggle(BookOfferVM bookOfferVM);
        boolean isInFavorite(BookOfferVM bookOfferVM);
    }
}
