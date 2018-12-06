package ba.lukic.petar.eknjiznica.ui.book_details;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;

public class BookDetailsPresenter implements BookDetailsContract.Presenter {
    IBookRepo bookRepo;
    private BookDetailsContract.View view;
    @Inject
    public BookDetailsPresenter(IBookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }



    @Override
    public void toggleFavorite(BookOfferVM bookOfferVM) {
        List<BookOfferVM> books = bookRepo.GetFavoriteBooks();

        boolean contains = books.contains(bookOfferVM);
        if(contains) {
            books.remove(bookOfferVM);
        }else {
            books.add(bookOfferVM);
        }
        bookRepo.SetFavoriteBooks(books);
        view.toggleFavoriteIcon(!contains);
    }

    @Override
    public boolean isInFavorite(int bookId) {
        return bookRepo.GetFavoriteBooks().contains(bookId);
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

    }
}
