package ba.lukic.petar.eknjiznica.ui.shopping_cart;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookBuyEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.util.BaseErrorFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;

import static io.reactivex.Observable.fromCallable;

public class ShoppingCartPresenter implements ShoppingCartContract.Presenter {
    private ShoppingCartContract.View view;

    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;
    private BaseErrorFactory baseErrorFactory;
    private Disposable subscribe;
    private EventBus eventBus;
    @Inject
    public ShoppingCartPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider, BaseErrorFactory baseErrorFactory, EventBus eventBus) {
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
        this.baseErrorFactory = baseErrorFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void deleteBookFromShoppingCart(BookOfferVM bookOfferVM) {
        List<BookOfferVM> bookOfferVMS = bookRepo.GetBasketBooks();
        bookOfferVMS.remove(bookOfferVM);
        bookRepo.SetBasketBooks(bookOfferVMS);
        onBooksLoaded(bookOfferVMS);
    }

    @Override
    public void buyBooks() {
        if (subscribe != null)
            subscribe.dispose();
        subscribe = fromCallable(() -> bookRepo.GetBasketBooks())
                .flatMapCompletable(bookOfferVMS -> bookRepo.BuyBooks(bookOfferVMS))
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .andThen((CompletableSource) co -> {
                    bookRepo.SetBasketBooks(new ArrayList<>());
                    onBooksLoaded(new ArrayList<>());
                    co.onComplete();
                })
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(() -> {
                            view.displayLoading(false);
                            view.displayBooksSentToEmail();
                            eventBus.post(new BookBuyEvent());
                        },
                        throwable -> {
                            view.displayLoading(false);
                            String error = baseErrorFactory.parseSingleError(throwable, "buy_book");
                            view.displayError(error);
                        });
    }

    private void onBooksLoaded(List<BookOfferVM> bookOfferVMS) {
        double totalPrice = calculateTotalPrice(bookOfferVMS);
        double taxes = calculateTaxes(totalPrice);
        double price = calculatePrice(totalPrice, taxes);

        view.toggleBuyButton(!bookOfferVMS.isEmpty());
        view.displayPrice(totalPrice, price, taxes);
        view.displayShoppingCartBooks(bookOfferVMS);
    }

    @Override
    public void loadBooks() {
        List<BookOfferVM> bookOfferVMS = bookRepo.GetBasketBooks();
        onBooksLoaded(bookOfferVMS);
    }

    @Override
    public void takeView(ShoppingCartContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    public double calculateTotalPrice(List<BookOfferVM> bookOfferVMS) {
        double price = 0;
        for (BookOfferVM vm : bookOfferVMS) {
            price += vm.Price;
        }
        return price;
    }

    public double calculateTaxes(double price) {
        return price * 0.17;
    }

    public double calculatePrice(double total, double tax) {
        return total - tax;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (subscribe != null)
            subscribe.dispose();
    }
}
