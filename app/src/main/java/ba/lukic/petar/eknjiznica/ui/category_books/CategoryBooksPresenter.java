package ba.lukic.petar.eknjiznica.ui.category_books;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.BuildConfig;
import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class CategoryBooksPresenter extends BooksPresenter<CategoryBooksContract.View> implements CategoryBooksContract.Presenter {
    private CategoryBooksContract.View view;
    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;
    private Disposable subscribe;


    @Inject
    public CategoryBooksPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider, EventBus eventBus) {
        super(bookRepo, eventBus);
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void loadBooks(int categoryId,String bookName) {
        if (subscribe != null)
            subscribe.dispose();
        subscribe = bookRepo.GetBooks(categoryId,bookName)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .map(bookOfferVMS -> {
                    for (BookOfferVM bookOfferVM:bookOfferVMS) {
                        bookOfferVM.ImageUrl= BuildConfig.BASE_URL + "/"+bookOfferVM.ImageUrl;
                    }
                    return bookOfferVMS;
                })
                .subscribe(bookOfferVMS -> {
                    view.displayLoading(false);
                    view.displayBooks(bookOfferVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

    @Override
    public void takeView(CategoryBooksContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
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
