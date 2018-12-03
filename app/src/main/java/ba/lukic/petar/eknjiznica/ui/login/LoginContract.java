package ba.lukic.petar.eknjiznica.ui.login;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;

public interface LoginContract {
    interface View extends BaseAsyncView<Presenter>{

        void displayPleaseFillInForm();

        void displayLoginError(String error);

        void navigateToHomeScreen();

    }
    interface Presenter extends BasePresenter<View>{
        boolean isValidPassword(String password);
        boolean isValidUsername(String username);

        void signIn(String username, String password);
    }
}
