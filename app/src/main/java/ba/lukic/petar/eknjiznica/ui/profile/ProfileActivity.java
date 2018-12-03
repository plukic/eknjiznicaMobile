package ba.lukic.petar.eknjiznica.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ProfileActivity extends BaseDaggerAuthorizedActivity implements ProfileContract.View {

    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    DialogFactory dialogFactory;
    @Inject
    IAccountRepo accountRepo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.ed_firstname)
    EditText edFirstname;
    @BindView(R.id.til_firstname)
    TextInputLayout tilFirstname;
    @BindView(R.id.ed_last_name)
    EditText edLastName;
    @BindView(R.id.til_lastname)
    TextInputLayout tilLastname;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.ed_phone_number)
    EditText edPhoneNumber;
    @BindView(R.id.til_phone_number)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.ed_account_balance)
    EditText edAccountBalance;
    @BindView(R.id.til_account_balance)
    TextInputLayout tilAccountBalance;


    @Inject
    ProfileContract.Presenter presenter;
    @BindView(R.id.parent)
    ConstraintLayout parent;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        loadProfileInfo();
        setupValidation();
        presenter.takeView(this);
        presenter.onStart();
    }

    private void loadProfileInfo() {
        Disposable subscribe = accountRepo.loadUserProfileInfo()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(clientsDetailsModel -> {
                    edFirstname.setText(clientsDetailsModel.FirstName);
                    edLastName.setText(clientsDetailsModel.LastName);
                    edUsername.setText(clientsDetailsModel.UserName);
                    edEmail.setText(clientsDetailsModel.Email);
                    edPhoneNumber.setText(clientsDetailsModel.PhoneNumber);
                    edAccountBalance.setText(String.format("%sKM", clientsDetailsModel.AccountBalance));
                }, Timber::e);

        compositeDisposable.add(subscribe);
    }

    private void setupValidation() {

        Resources r = getResources();

        compositeDisposable.add(RxTextView
                .textChanges(edFirstname)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidFirstName(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_first_name_invalid))
                .subscribe(error -> tilFirstname.setError(error)));

        compositeDisposable.add(RxTextView
                .textChanges(edLastName)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidLastName(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_last_name_invalid))
                .subscribe(error -> tilLastname.setError(error)));

        compositeDisposable.add(RxTextView
                .textChanges(edUsername)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidUsername(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_username_invalid))
                .subscribe(error -> tilUsername.setError(error)));


        compositeDisposable.add(RxTextView
                .textChanges(edPhoneNumber)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidPhoneNumber(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_phone_invalid))
                .subscribe(error -> tilPhoneNumber.setError(error)));


        compositeDisposable.add(RxTextView
                .textChanges(edEmail)
                .observeOn(schedulersProvider.main())
                .map(charSequence -> presenter.isValidEmailAddress(charSequence.toString()))
                .map(isValid -> isValid ? "" : r.getString(R.string.error_field_email_invalid))
                .subscribe(error -> tilEmail.setError(error)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        presenter.onStop();
        presenter.dropView();
    }

    @OnClick(R.id.fab)
    public void onUpdateProfile(){
        String firstName = edFirstname.getText().toString();
        String lastName = edLastName.getText().toString();
        String phoneNumber = edPhoneNumber.getText().toString();
        String email = edEmail.getText().toString();
        presenter.updateUser(firstName, lastName, email,  phoneNumber);

    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, ProfileActivity.class);
    }

    @Override
    public void displayFillFormsError() {
        Snackbar.make(parent, R.string.error_fill_in_fields, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String error) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void displayUpdateSuccessfully() {
        Snackbar.make(parent, R.string.profile_update_success, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        if (progressDialog != null)
            progressDialog.dismiss();
        if (isLoading) {
            progressDialog = dialogFactory.createProgressDialog(R.string.please_wait);
            progressDialog.show();
        }
    }
}
