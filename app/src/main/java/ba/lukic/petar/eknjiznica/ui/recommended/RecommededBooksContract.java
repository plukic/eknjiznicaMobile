package ba.lukic.petar.eknjiznica.ui.recommended;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public interface RecommededBooksContract {
    interface  Presenter extends BasePresenter<View> {
        void loadBooks();

        void onFavoriteToggle(BookOfferVM bookOfferVM);
        boolean isInFavorite(BookOfferVM bookOfferVM);
    }
    interface View extends BaseAsyncView<Presenter>{
        void  displayNoRecommededBooks();
        void displayNoTopSellingBooks();

        void displayTopSellingBooks(List<BookOfferVM> bookOfferVMList);
        void displayRecommendedBooks(List<BookOfferVM> bookOfferVMList);

        void displayLoadingRecommended(boolean b);
        void displayLoadingTopSelling(boolean b);
    }
}
