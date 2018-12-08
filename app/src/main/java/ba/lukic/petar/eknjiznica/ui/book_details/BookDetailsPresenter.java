package ba.lukic.petar.eknjiznica.ui.book_details;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class BookDetailsPresenter extends BooksPresenter<BookDetailsContract.View> implements BookDetailsContract.Presenter {
    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;
    private BookDetailsContract.View view;
    private Disposable subscribe;

    @Inject
    public BookDetailsPresenter(IBookRepo bookRepo, EventBus eventBus, ISchedulersProvider schedulersProvider) {
        super(bookRepo,eventBus);
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
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
        if(subscribe!=null)
            subscribe.dispose();
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

    @Override
    public void downloadBook(BookOfferVM book) {
        subscribe = bookRepo.downloadBook(book.BookId)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displaySendingBookToEmail(true))
                .subscribe(() -> {
                    view.displaySendingBookToEmail(false);
                    view.displayBookSendSuccessfully();
                }, throwable -> {
                    view.displaySendingBookToEmail(false);
                    view.displayBookSendingError();
                });
    }
}
