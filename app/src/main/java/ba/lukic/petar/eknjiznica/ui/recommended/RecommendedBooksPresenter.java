package ba.lukic.petar.eknjiznica.ui.recommended;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.BuildConfig;
import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RecommendedBooksPresenter extends BooksPresenter<RecommededBooksContract.View> implements RecommededBooksContract.Presenter {
    private RecommededBooksContract.View view;

    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;

    private CompositeDisposable compositeDisposable;
    @Inject
    public RecommendedBooksPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider, EventBus eventBus) {
        super(bookRepo,eventBus);
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
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
    public void takeView(RecommededBooksContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view=null;
    }

    @Override
    public void onStart() {
        compositeDisposable=new CompositeDisposable();
    }

    @Override
    public void onStop() {
        compositeDisposable.dispose();
    }
}
