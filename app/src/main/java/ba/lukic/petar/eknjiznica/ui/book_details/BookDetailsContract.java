package ba.lukic.petar.eknjiznica.ui.book_details;

import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public interface BookDetailsContract {

    interface View extends BaseView<Presenter> {
        void toggleFavoriteIcon(boolean isFavorite);
    }

    interface Presenter extends BasePresenter<View> {
        void toggleFavorite(BookOfferVM bookOfferVM);

        boolean isInFavorite(int bookId);
    }
}
