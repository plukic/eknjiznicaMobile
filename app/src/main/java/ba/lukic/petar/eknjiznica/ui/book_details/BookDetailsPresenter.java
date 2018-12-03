package ba.lukic.petar.eknjiznica.ui.book_details;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.books.IBookRepo;

public class BookDetailsPresenter implements BookDetailsContract.Presenter {
    IBookRepo bookRepo;
    private BookDetailsContract.View view;

    @Inject
    public BookDetailsPresenter(IBookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public void toggleFavorite(Integer bookId) {
        List<Integer> integers = bookRepo.GetFavoriteBooks();
        boolean contains = integers.contains(bookId);
        if(contains) {
            integers.remove(bookId);
        }else {
            integers.add(bookId);
        }
        bookRepo.SetFavoriteBooks(integers);
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
