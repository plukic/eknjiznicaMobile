package ba.lukic.petar.eknjiznica.ui.books;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public abstract class BooksPresenter<T> implements BooksContract.Presenter<T> {
    protected T view;

    private IBookRepo bookRepo;
    private EventBus eventBus;

    public BooksPresenter(IBookRepo bookRepo, EventBus eventBus) {
        this.bookRepo = bookRepo;
        this.eventBus = eventBus;
    }

    @Override
    public void onFavoriteToggle(BookOfferVM bookOfferVM) {
        List<BookOfferVM> integers = bookRepo.GetFavoriteBooks();

        boolean contains = integers.contains(bookOfferVM);
        if(contains){
            integers.remove((bookOfferVM));
        }else{
            integers.add(bookOfferVM);
        }
        bookRepo.SetFavoriteBooks(integers);
        FavoriteBookToggleEvent event = new FavoriteBookToggleEvent(bookOfferVM,true);
        eventBus.post(event);

    }

    @Override
    public boolean isInFavorite(BookOfferVM bookOfferVM) {
        return bookRepo.GetFavoriteBooks().contains(bookOfferVM);
    }


    @Override
    public void takeView(T view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view=null;
    }

}
