package ba.lukic.petar.eknjiznica.data.books;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.api.IApiService;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPrefsRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
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
}
