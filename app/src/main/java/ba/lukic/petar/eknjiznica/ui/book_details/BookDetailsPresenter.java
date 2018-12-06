package ba.lukic.petar.eknjiznica.ui.book_details;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;

public class BookDetailsPresenter extends BooksPresenter<BookDetailsContract.View> implements BookDetailsContract.Presenter {
    IBookRepo bookRepo;
    private BookDetailsContract.View view;
    @Inject
    public BookDetailsPresenter(IBookRepo bookRepo,EventBus eventBus) {
        super(bookRepo,eventBus);
        this.bookRepo = bookRepo;
    }


    @Override
    public void onFavoriteToggle(BookOfferVM bookOfferVM, boolean displayNotification) {
        super.onFavoriteToggle(bookOfferVM, displayNotification);
        boolean inFavorite = isInFavorite(bookOfferVM);
        view.toggleFavoriteIcon(inFavorite);
    }

    @Override
    public void takeView(BookDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view=null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void addBookToBasket(BookOfferVM bookOfferVM) {
        List<BookOfferVM> bookOfferVMS = bookRepo.GetBasketBooks();
        if(bookOfferVMS.contains(bookOfferVM)){
            view.displayBookAddedToBasket();
        }else{
            bookOfferVMS.add(bookOfferVM);
            bookRepo.SetBasketBooks(bookOfferVMS);
            view.displayBookAddedToBasket();

        }

    }
}
