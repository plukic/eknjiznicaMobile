package ba.lukic.petar.eknjiznica.data.books;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.api.IApiService;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPrefsRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class BookRepo implements IBookRepo {
    @Inject
    IApiService apiService;

    @Inject
    SharedPrefsRepo sharedPrefsRepo;

    @Inject
    public BookRepo(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<List<BookOfferVM>> GetTopSellingBooks() {
        return apiService.GetTopSellingBooks();
    }

    @Override
    public Observable<List<BookOfferVM>> GetRecommendedBooks() {
        return apiService.GetRecommendedBooks();
    }

    @Override
    public List<BookOfferVM> GetFavoriteBooks(){
        return sharedPrefsRepo.getFavoriteBooks();
    }

    @Override
    public void SetFavoriteBooks(List<BookOfferVM> books){
        sharedPrefsRepo.saveFavoriteBooks(books);
    }

    @Override
    public Observable<List<CategoryVM>> GetCategories() {
        return apiService.GetCategories();

    }

    @Override
    public Observable<List<CategoryVM>> GetTopSellingCategories() {
        return apiService.GetTopSellingCategories();
    }

    @Override
    public void SetBasketBooks(List<BookOfferVM> bookOfferVMS) {
         sharedPrefsRepo.saveBasketBooks(bookOfferVMS);

    }

    @Override
    public List<BookOfferVM> GetBasketBooks() {
        return sharedPrefsRepo.getBasketBooks();
    }

    @Override
    public Completable downloadBook(int bookId) {
        return apiService.DownloadBook(bookId);
    }

    @Override
    public Completable BuyBooks(List<BookOfferVM> bookOfferVMS) {
        return apiService.BuyBook(bookOfferVMS);    }

    @Override
    public Observable<List<ClientBookVM>> LoadMyBooks(String bookName) {
        return apiService.LoadMyBooks(bookName);
    }

    @Override
    public Observable<List<BookOfferVM>> GetBooks(int categoryId, String title) {

        return apiService.GetBooks(title,categoryId);
    }
}
