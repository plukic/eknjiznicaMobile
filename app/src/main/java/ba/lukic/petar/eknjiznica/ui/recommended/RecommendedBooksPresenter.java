package ba.lukic.petar.eknjiznica.ui.recommended;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.BuildConfig;
import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RecommendedBooksPresenter implements RecommededBooksContract.Presenter {
    private RecommededBooksContract.View view;

    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;

    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private EventBus eventBus;
    @Inject
    public RecommendedBooksPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider, EventBus eventBus) {
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
        this.eventBus = eventBus;
    }

    @Override
    public void loadBooks() {

        Disposable recommendedDisposable = bookRepo.GetRecommendedBooks()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoadingRecommended(true))
                .map(bookOfferVMS ->
                {
                    for (BookOfferVM vm:bookOfferVMS) {
                        vm.ImageUrl = BuildConfig.BASE_URL +"/"+vm.ImageUrl;
                    }
                    return bookOfferVMS;
                })
                .subscribe(bookOfferVMS -> {
                    view.displayLoadingRecommended(false);
                    if(bookOfferVMS.isEmpty())
                        view.displayNoRecommededBooks();
                    else
                        view.displayRecommendedBooks(bookOfferVMS);
                }, throwable -> {
                    view.displayLoadingRecommended(false);
                    view.displayUnexpectedError();
                });

        Disposable topSellingDisposable = bookRepo.GetTopSellingBooks()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoadingTopSelling( true))
                .map(bookOfferVMS ->
                {
                    for (BookOfferVM vm:bookOfferVMS) {
                        vm.ImageUrl = BuildConfig.BASE_URL +"/"+vm.ImageUrl;
                    }
                    return bookOfferVMS;
                })
                .subscribe(bookOfferVMS -> {
                    view.displayLoadingTopSelling(false);
                    if(bookOfferVMS.isEmpty())
                        view.displayNoTopSellingBooks();
                    else
                        view.displayTopSellingBooks(bookOfferVMS);
                }, throwable -> {
                    view.displayLoadingTopSelling(false);
                    view.displayUnexpectedError();
                });

        compositeDisposable.add(recommendedDisposable);
        compositeDisposable.add(topSellingDisposable);
    }


    @Override
    public void onFavoriteToggle(BookOfferVM bookOfferVM) {
        List<Integer> integers = bookRepo.GetFavoriteBooks();

        boolean contains = integers.contains(bookOfferVM.BookId);
        if(contains){
            integers.remove(((Integer) bookOfferVM.BookId));
        }else{
            integers.add(bookOfferVM.BookId);
        }
        bookRepo.SetFavoriteBooks(integers);
        eventBus.post(bookOfferVM);

    }

    @Override
    public boolean isInFavorite(BookOfferVM bookOfferVM) {
        return bookRepo.GetFavoriteBooks().contains(bookOfferVM.BookId);
    }

    @Override
    public void takeView(RecommededBooksContract.View view) {
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
        compositeDisposable.dispose();
    }
}
