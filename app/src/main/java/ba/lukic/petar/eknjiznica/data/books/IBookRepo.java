package ba.lukic.petar.eknjiznica.data.books;


import java.util.List;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public interface IBookRepo {
    Observable<List<BookOfferVM>> GetTopSellingBooks();
    Observable<List<BookOfferVM>> GetRecommendedBooks();

    List<BookOfferVM> GetFavoriteBooks();

    void SetFavoriteBooks(List<BookOfferVM> books);

    Observable<List<CategoryVM>> GetCategories();
    Observable<List<CategoryVM>> GetTopSellingCategories();

    void SetBasketBooks(List<BookOfferVM> bookOfferVMS);
    List<BookOfferVM> GetBasketBooks();

    Completable downloadBook(int bookId);

    Completable BuyBooks(List<BookOfferVM> bookOfferVMS);

    Observable<List<ClientBookVM>> LoadMyBooks(String bookName);
}
