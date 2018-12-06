package ba.lukic.petar.eknjiznica.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerActivity;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.ui.home.HomeActivity;
import ba.lukic.petar.eknjiznica.ui.register.RegisterActivity;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseDaggerActivity implements LoginContract.View {


    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    LoginContract.Presenter presenter;


    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    DialogFactory dialogFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private ProgressDialog progressDialog;

    public static Intent getInstance(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getInstanceWithMessage(Context ctx, String message) {
        return getInstance(ctx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupToolbar(toolbar,false,R.string.title_activity_login);
        presenter.takeView(this);
        presenter.onStart();
        setupValidation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
        compositeDisposable.dispose();
    }

    private void setupValidation() {
        Resources resources = getResources();

        Disposable s1 = RxTextView.textChanges(edUsername)
                .subscribeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidUsername(charSequence.toString()) ? "" : resources.getString(R.string.error_field_username_invalid))
                .subscribe(tilUsername::setError);

        Disposable s2 = RxTextView.textChanges(edPassword)
                .subscribeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidPassword(charSequence.toString()) ? "" : resources.getString(R.string.error_field_password_invalid))
                .subscribe(tilPassword::setError);

        compositeDisposable.add(s1);
        compositeDisposable.add(s2);
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignIn() {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();

        presenter.signIn(username, password);

    }

    @OnClick(R.id.btn_sign_up)
    public void onRegister() {
        startActivity(RegisterActivity.GetInstance(this));
    }

    @Override
    public void displayPleaseFillInForm() {
        Snackbar.make(parent, R.string.error_fill_in_fields, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoginError(String error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void navigateToHomeScreen() {
        Intent intent = HomeActivity.getInstance(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayLoading(boolean isLoading) {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (isLoading) {
            progressDialog = dialogFactory.createProgressDialog(R.string.please_wait);
            progressDialog.show();
        }
    }
}
