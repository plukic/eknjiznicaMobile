package ba.lukic.petar.eknjiznica.data.shared_prefs;

import com.google.gson.Gson;

import java.util.UUID;

import ba.lukic.petar.eknjiznica.model.login.AuthenticationResponse;
import ba.lukic.petar.eknjiznica.model.user.ClientsDetailsModel;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class SharedPrefsRepo {
    private static final String KEY_AUTHENTICATION_RESPONSE = "KEY_AUTHENTICATION_RESPONSE";
    private static final String KEY_CLIENT_PROFILE_INFO = "KEY_CLIENT_PROFILE_INFO";
    private static final String KEY_UNIQUE_ID = "KEY_UNIQUE_ID";
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
}
