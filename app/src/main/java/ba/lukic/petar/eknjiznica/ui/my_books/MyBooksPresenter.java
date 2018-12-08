package ba.lukic.petar.eknjiznica.ui.my_books;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.BuildConfig;
import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class MyBooksPresenter extends BooksPresenter<MyBooksContract.View> implements MyBooksContract.Presenter {
    private MyBooksContract.View view;
    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;
    private Disposable subscribe;


    @Inject
    public MyBooksPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider, EventBus eventBus) {
        super(bookRepo, eventBus);
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void loadBooks(String bookName) {
        if (subscribe != null)
            subscribe.dispose();
        subscribe = bookRepo.LoadMyBooks(bookName)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .map(clientBookVMS -> {
                    for (ClientBookVM clientBookVM:clientBookVMS) {
                        clientBookVM.FullImageUrl = BuildConfig.BASE_URL + "/"+clientBookVM.ImageUrl;
                    }
                    return clientBookVMS;
                })
                .subscribe(clientBookVMS -> {
                    view.displayLoading(false);
                    view.displayMyBooks(clientBookVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

    @Override
    public void takeView(MyBooksContract.View view) {
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
