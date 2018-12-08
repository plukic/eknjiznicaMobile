package ba.lukic.petar.eknjiznica.ui.books;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;

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
        if (contains) {
            integers.remove((bookOfferVM));
        } else {
            bookOfferVM.AddedToFavorites = DateTime.now();
            integers.add(bookOfferVM);
        }
        bookRepo.SetFavoriteBooks(integers);
        FavoriteBookToggleEvent event = new FavoriteBookToggleEvent(bookOfferVM, true);
        eventBus.post(event);

    }

    @Override
    public void onFavoriteToggle(BookOfferVM bookOfferVM, boolean displayNotification) {
        List<BookOfferVM> integers = bookRepo.GetFavoriteBooks();

        boolean contains = integers.contains(bookOfferVM);
        if (contains) {
            integers.remove((bookOfferVM));
        } else {
            bookOfferVM.AddedToFavorites = DateTime.now();
            integers.add(bookOfferVM);
        }
        bookRepo.SetFavoriteBooks(integers);
        FavoriteBookToggleEvent event = new FavoriteBookToggleEvent(bookOfferVM, displayNotification);
        eventBus.post(event);

    }

    @Override
    public boolean isInFavorite(BookOfferVM bookOfferVM) {
        return bookRepo.GetFavoriteBooks().contains(bookOfferVM);
    }


    @Override
    public void onFavoriteToggle(ClientBookVM clientBookOffer) {

        List<BookOfferVM> favorites = bookRepo.GetFavoriteBooks();
        boolean contains = false;
        BookOfferVM e=null;
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).BookId == clientBookOffer.BookId) {
                e=favorites.get(i);
                favorites.remove(i);
                contains = true;
                break;
            }
        }
        if (!contains) {
            e = new BookOfferVM(clientBookOffer);
            e.AddedToFavorites=DateTime.now();
            favorites.add(e);
        }
        bookRepo.SetFavoriteBooks(favorites);
        FavoriteBookToggleEvent event = new FavoriteBookToggleEvent(e,true);
        eventBus.post(event);

    }

    @Override
    public void onFavoriteToggle(ClientBookVM clientBookOffer, boolean displayNotification) {
        List<BookOfferVM> favorites = bookRepo.GetFavoriteBooks();
        boolean contains = false;
        BookOfferVM e=null;
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).BookId == clientBookOffer.BookId) {
                e=favorites.get(i);
                favorites.remove(i);
                contains = true;
                break;
            }
        }
        if (!contains) {
            e = new BookOfferVM(clientBookOffer);
            e.AddedToFavorites=DateTime.now();
            favorites.add(e);
        }
        bookRepo.SetFavoriteBooks(favorites);
        FavoriteBookToggleEvent event = new FavoriteBookToggleEvent(e, displayNotification);
        eventBus.post(event);

    }

    @Override
    public boolean isInFavorite(ClientBookVM bookOfferVM) {
        List<BookOfferVM> bookOfferVMS = bookRepo.GetFavoriteBooks();
        for (BookOfferVM vm : bookOfferVMS) {
            if(vm.BookId==bookOfferVM.BookId)
                return true;
        }
        return false;
    }


    @Override
    public void takeView(T view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

}
