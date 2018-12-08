package ba.lukic.petar.eknjiznica.ui.shopping_cart;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public interface ShoppingCartContract {
    interface  Presenter extends BasePresenter<View>{
        void  deleteBookFromShoppingCart(BookOfferVM bookOfferVM);
        void buyBooks();
        void loadBooks();
    }
    interface  View extends BaseAsyncView<Presenter>{
        void displayShoppingCartBooks(List<BookOfferVM> bookOfferVMList);
        void displayPrice(double total, double price,double tax);
        void toggleBuyButton(boolean isEnabled);
        void displayBooksSentToEmail();

        void displayError(String errror);
    }
}
