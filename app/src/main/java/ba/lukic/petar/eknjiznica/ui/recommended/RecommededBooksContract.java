package ba.lukic.petar.eknjiznica.ui.recommended;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.base.BaseView;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;

public interface RecommededBooksContract {
    interface  Presenter extends BooksContract.Presenter<View> {
        void loadBooks();

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
