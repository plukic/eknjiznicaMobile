package ba.lukic.petar.eknjiznica.data.books;


import java.util.List;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import io.reactivex.Observable;

public interface IBookRepo {
    Observable<List<BookOfferVM>> GetTopSellingBooks();
    Observable<List<BookOfferVM>> GetRecommendedBooks();

    List<BookOfferVM> GetFavoriteBooks();

    void SetFavoriteBooks(List<BookOfferVM> books);

    Observable<List<CategoryVM>> GetCategories();
    Observable<List<CategoryVM>> GetTopSellingCategories();

}
