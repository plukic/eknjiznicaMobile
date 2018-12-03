package ba.lukic.petar.eknjiznica.data.books;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.data.api.IApiService;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPrefsRepo;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
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
    public List<Integer> GetFavoriteBooks(){
        return sharedPrefsRepo.getFavoriteBooks();
    }

    @Override
    public void SetFavoriteBooks(List<Integer> books){
        sharedPrefsRepo.saveFavoriteBooks(books);
    }
}
