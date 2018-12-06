package ba.lukic.petar.eknjiznica.ui.favorite_books;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksPresenter;

public class FavoriteBooksPresenter  extends BooksPresenter<FavoriteBooksContract.View> implements FavoriteBooksContract.Presenter {

    private final IBookRepo bookRepo;

    @Inject
    public FavoriteBooksPresenter(IBookRepo bookRepo, EventBus eventBus) {
        super(bookRepo, eventBus);
        this.bookRepo = bookRepo;
    }

    @Override
    public void loadFavoriteBooks() {
        view.displayLoading(false);
        List<BookOfferVM> bookOfferVMS = bookRepo.GetFavoriteBooks();
        if(bookOfferVMS.isEmpty()){
            view.displayNoFavoriteBooks();
        }else{
            view.displayFavoriteBooks(bookOfferVMS);
        }

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
