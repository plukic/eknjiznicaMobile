package ba.lukic.petar.eknjiznica.ui.categories;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private CategoriesContract.View view;

    private IBookRepo bookRepo;
    private ISchedulersProvider schedulersProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CategoriesPresenter(IBookRepo bookRepo, ISchedulersProvider schedulersProvider) {
        this.bookRepo = bookRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void loadCategories() {
        Disposable subscribe = bookRepo.GetCategories()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(categoryVMS -> {
                    view.displayLoading(false);
                    if(categoryVMS.isEmpty())
                        view.displayNoCategories();
                    else
                        view.displayCategories(categoryVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });

        Disposable subscribe2 = bookRepo.GetTopSellingCategories()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(categoryVMS -> {
                    view.displayLoading(false);
                    if(categoryVMS.isEmpty())
                        view.displayNoMostPopularCategories();
                    else
                        view.displayMostPopularCategories(categoryVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });

        compositeDisposable.add(subscribe);
        compositeDisposable.add(subscribe2);
    }

    @Override
    public void takeView(CategoriesContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
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
