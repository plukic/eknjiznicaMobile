package ba.lukic.petar.eknjiznica.data.books;


import java.util.List;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import io.reactivex.Observable;

public interface IBookRepo {
    Observable<List<BookOfferVM>> GetTopSellingBooks();
    Observable<List<BookOfferVM>> GetRecommendedBooks();

    List<Integer> GetFavoriteBooks();

    void SetFavoriteBooks(List<Integer> books);
}
