package ba.lukic.petar.eknjiznica.data.shared_prefs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.login.AuthenticationResponse;
import ba.lukic.petar.eknjiznica.model.user.ClientsDetailsModel;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class SharedPrefsRepo {
    private static final String KEY_AUTHENTICATION_RESPONSE = "KEY_AUTHENTICATION_RESPONSE";
    private static final String KEY_CLIENT_PROFILE_INFO = "KEY_CLIENT_PROFILE_INFO";
    private static final String KEY_UNIQUE_ID = "KEY_UNIQUE_ID";
    private static final String KEY_FAVORITE_BOOKS = "KEY_FAVORITE_BOOKS";
    private static final String KEY_BASKET_BOOKS = "KEY_BASKET_BOOKS";
    private SharedPreferenceHelper helper;
    private Gson gson;

    public SharedPrefsRepo(SharedPreferenceHelper helper, Gson gson) {
        this.helper = helper;
        this.gson = gson;
    }

    public Observable<AuthenticationResponse> getAuthenticationResponse() {
        return Observable.fromCallable(() -> {
            String sharedPreferenceString = helper.getSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, null);
            AuthenticationResponse response = gson.fromJson(sharedPreferenceString, AuthenticationResponse.class);

            if (response == null)
                response = new AuthenticationResponse();

            return response;
        });
    }

    public void setAuthenticationResponse(AuthenticationResponse authenticationResponse) {
        helper.setSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, gson.toJson(authenticationResponse));
    }

    public void setClientInfo(ClientsDetailsModel clientInfo) {
        helper.setSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, gson.toJson(clientInfo));
    }

    public ClientsDetailsModel getClientInfo() {
        String sharedPreferenceString = helper.getSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, null);
        return gson.fromJson(sharedPreferenceString, ClientsDetailsModel.class);
    }

    public Completable LogoutCurrentUser() {
        return Completable.create(emitter -> {
            helper.setSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, null);
            helper.setSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, null);
            emitter.onComplete();
        });
    }


    public String GetUniqueId() {
        String uniqueId = helper.getSharedPreferenceString(KEY_UNIQUE_ID, null);
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
            helper.setSharedPreferenceString(KEY_UNIQUE_ID, uniqueId);
        }
        return uniqueId;
    }

    public List<BookOfferVM> getFavoriteBooks() {
        try{
            String json = helper.getSharedPreferenceString(KEY_FAVORITE_BOOKS, "");
            Type listType = new TypeToken<ArrayList<BookOfferVM>>() {}.getType();
            List<BookOfferVM> yourClassList = gson.fromJson(json, listType);
            if (yourClassList == null)
                yourClassList = new ArrayList<>();
            return yourClassList;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public void saveBasketBooks(List<BookOfferVM> bookId) {
        String json = gson.toJson(bookId);
        helper.setSharedPreferenceString(KEY_BASKET_BOOKS, json);
    }

    public List<BookOfferVM> getBasketBooks() {
        try{
            String json = helper.getSharedPreferenceString(KEY_BASKET_BOOKS, "");
            Type listType = new TypeToken<ArrayList<BookOfferVM>>() {}.getType();
            List<BookOfferVM> yourClassList = gson.fromJson(json, listType);
            if (yourClassList == null)
                yourClassList = new ArrayList<>();
            return yourClassList;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public void saveFavoriteBooks(List<BookOfferVM> bookId) {
        String json = gson.toJson(bookId);
        helper.setSharedPreferenceString(KEY_FAVORITE_BOOKS, json);
    }
}
