package ba.lukic.petar.eknjiznica.data.account;


import ba.lukic.petar.eknjiznica.model.login.AuthenticationResponse;
import ba.lukic.petar.eknjiznica.model.user.ClientAddVM;
import ba.lukic.petar.eknjiznica.model.user.ClientUpdateVM;
import ba.lukic.petar.eknjiznica.model.user.ClientsDetailsModel;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface IAccountRepo {
    Observable<AuthenticationResponse> loginUser(String username, String password);
    Observable<ClientsDetailsModel> loadUserProfileInfo();

    Completable LogoutCurrentUser();

//    Completable confirmPasswordReset(String username, String password, String token);
//
//    Completable requestPasswordResetToken(String username);


    Observable<Boolean> isUserLogged();

    Completable registerUser(ClientAddVM clientCreateModel);

    Completable updateUser(ClientUpdateVM clientUpdateVM);


//    Observable<SettingsVM> getUserSettings();
//
//    Completable ToggleSettings(SettingsFragment.SettingsUpdate type);
//
//    boolean IsAutomaticCrashDetectionServiceEnabled();
//
//    boolean areNotificationsEnabled();
//
//    Single<LatLng> getUserLastParkedLocation();
//
//    Completable setUserLastParkedLocation(LatLng latLng);
//
//    Observable<String> GetDeviceUniqueId();
//
//    Completable updateUserAccountInformation(ClientAccountUpdateVM accountUpdateVM);
//
//    Observable<List<EmergencyContactNumbers>> GetEmergencyContacts();
//
//    Completable deleteEmergencyContact(EmergencyContactNumbers emergencyContactNumber);
//
//    Completable updateEmergencyContact(EmergencyContactNumbers emergencyContactNumber);
//
//    Completable createEmergencyContact(EmergencyContactNumbers emergencyContactNumber);
}
