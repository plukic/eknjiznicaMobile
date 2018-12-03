package ba.lukic.petar.eknjiznica.data.account;


import javax.inject.Inject;
import javax.inject.Named;

import ba.lukic.petar.eknjiznica.data.api.IApiService;
import ba.lukic.petar.eknjiznica.data.shared_prefs.SharedPrefsRepo;
import ba.lukic.petar.eknjiznica.model.login.AuthenticationResponse;
import ba.lukic.petar.eknjiznica.model.user.ClientAddVM;
import ba.lukic.petar.eknjiznica.model.user.ClientUpdateVM;
import ba.lukic.petar.eknjiznica.model.user.ClientsDetailsModel;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class AccountRepo implements IAccountRepo {
    private IApiService apiService;
    private SharedPrefsRepo sharedPrefsRepo;
    private String clientId;


    @Inject
    public AccountRepo(IApiService apiService, SharedPrefsRepo sharedPrefsRepo, @Named("client_id") String clientId) {
        this.apiService = apiService;
        this.sharedPrefsRepo = sharedPrefsRepo;
        this.clientId = clientId;
    }


    @Override
    public Observable<AuthenticationResponse> loginUser(String username, String password) {
        return apiService
                .LoginUser("password", username, password, this.clientId)
                .map(authenticationResponse -> {
                    sharedPrefsRepo.setAuthenticationResponse(authenticationResponse);
                    return authenticationResponse;
                });
    }

    @Override
    public Observable<ClientsDetailsModel> loadUserProfileInfo() {
        return apiService.GetClientProfileInfo()
                .map(clientInfo -> {
                    sharedPrefsRepo.setClientInfo(clientInfo);
                    return clientInfo;
                })
                .onErrorResumeNext((throwable) -> {

                    return Observable.just(sharedPrefsRepo.getClientInfo());
                });
    }

    @Override
    public Completable LogoutCurrentUser() {
        return sharedPrefsRepo.LogoutCurrentUser();
    }



    @Override
    public Observable<Boolean> isUserLogged() {
        return sharedPrefsRepo.getAuthenticationResponse()
                .flatMap(authenticationResponse -> Observable.just(sharedPrefsRepo.getClientInfo()))
                .flatMap(clientAccountInfo -> Observable.just(true))
                .onErrorResumeNext(throwable -> {
                    return Observable.just(false);
                });
    }

    @Override
    public Completable registerUser(ClientAddVM clientCreateModel) {

        return apiService.registerUser(clientCreateModel);
    }

    @Override
    public Completable updateUser(ClientUpdateVM clientUpdateVM) {

        return apiService.updateUser(clientUpdateVM);
    }


}
