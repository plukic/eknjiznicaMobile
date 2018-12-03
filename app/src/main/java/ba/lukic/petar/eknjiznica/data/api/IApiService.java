package ba.lukic.petar.eknjiznica.data.api;


import java.util.List;

import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.login.AuthenticationResponse;
import ba.lukic.petar.eknjiznica.model.user.ClientAddVM;
import ba.lukic.petar.eknjiznica.model.user.ClientUpdateVM;
import ba.lukic.petar.eknjiznica.model.user.ClientsDetailsModel;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IApiService {
    @POST("token")
    @FormUrlEncoded
    Observable<AuthenticationResponse> LoginUser(@Field("grant_type") String grant_type,
                                                 @Field("username") String username,
                                                 @Field("password") String password,
                                                 @Field("client_id") String clientId);

    @POST("token")
    @FormUrlEncoded
    Observable<AuthenticationResponse> RefreshUserToken(@Field("grant_type") String grantType,
                                                        @Field("refresh_token") String refreshToken,
                                                        @Field("client_id") String clientId);

    @GET("api/clients/profile")
    Observable<ClientsDetailsModel> GetClientProfileInfo();

    @POST("api/clients")
    Completable registerUser(@Body ClientAddVM clientCreateModel);

    @PUT("api/clients/profile")
    Completable updateUser(@Body ClientUpdateVM clientUpdateVM);
    @GET("api/books/recommended")
    Observable<List<BookOfferVM>> GetRecommendedBooks();
    @GET("api/books/topselling")
    Observable<List<BookOfferVM>> GetTopSellingBooks();
}