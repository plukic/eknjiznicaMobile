package ba.lukic.petar.eknjiznica.ui.book_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookDetailsActivity extends BaseDaggerAuthorizedActivity implements BookDetailsContract.View {


    private static final String PARCELABLE_BOOK_OFFER = "PARCELABLE_BOOK_OFFER";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.book_image)
    ImageView bookImage;
    @BindView(R.id.tv_book_title)
    TextView tvBookTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_book_categories)
    TextView tvBookCategories;
    @BindView(R.id.tv_label_price)
    TextView tvLabelPrice;
    @BindView(R.id.tv_book_price)
    TextView tvBookPrice;
    @BindView(R.id.tv_label_description)
    TextView tvLabelDescription;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.fab_shopping_cart)
    FloatingActionButton fabShoppingCart;
    @BindView(R.id.parent)
    CoordinatorLayout parent;

    @Inject
    BookDetailsContract.Presenter presenter;
    @Inject
    EventBus eventBus;
    @Inject
    DialogFactory dialogFactory;

    private BookOfferVM book;
    private ProgressDialog progressDialog;

    public static Intent GetInstance(BookOfferVM bookOfferVM, Context ctx) {
        Intent i = new Intent(ctx, BookDetailsActivity.class);
        i.putExtra(PARCELABLE_BOOK_OFFER, bookOfferVM);

        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);

        setupToolbar(toolbar, true, R.string.title_activity_book_details);

        book = getIntent().getParcelableExtra(PARCELABLE_BOOK_OFFER);


        tvBookTitle.setText(book.Title);
        tvBookPrice.setText(String.format("%s KM", book.Price));
        tvAuthor.setText(book.AuthorName);
        tvDescription.setText(book.Description);
        tvBookCategories.setText(getCategories(book.Categories));
        Glide.with(this).load(book.ImageUrl).apply(new RequestOptions().error(android.R.color.white).placeholder(android.R.color.white)).into(bookImage);
        presenter.takeView(this);

        toggleShoppingCartIcon();
    }

    private String getCategories(List<CategoryVM> categories) {
        if(categories.isEmpty())
            return null;
        return TextUtils.join(", ",categories);
    }

    private void toggleShoppingCartIcon() {
        if (book.UserHasBook) {
            fabShoppingCart.setImageResource(R.drawable.ic_cloud_download_black_24dp);
        } else {
            fabShoppingCart.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
        }
    }

    @OnClick(R.id.fab_shopping_cart)
    public void onShoppingCart() {
        if (book.UserHasBook)
            presenter.downloadBook(book);
        else
            presenter.addBookToBasket(book);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.menu_favorite) {
            presenter.onFavoriteToggle(book, false);
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_details_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem settingsItem = menu.findItem(R.id.menu_favorite);
        // set your desired icon here based on a flag if you like

        boolean inFavorite = presenter.isInFavorite(book);
        if (inFavorite) {
            settingsItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            settingsItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void toggleFavoriteIcon(boolean isFavorite) {
        invalidateOptionsMenu();

        Snackbar make;
        if (isFavorite) {
            make = Snackbar.make(parent, String.format(getString(R.string.book_favorite_added), book.Title), Snackbar.LENGTH_LONG);
        } else {
            make = Snackbar.make(parent, String.format(getString(R.string.book_favorite_removed), book.Title), Snackbar.LENGTH_LONG);
        }
        make.setAction(R.string.undo, view -> {
            presenter.onFavoriteToggle(book, false);
        });
        make.show();
    }

    @Override
    public void displayBookAddedToBasket() {
        Snackbar.make(parent, getString(R.string.book_added_to_basket), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayBookSendSuccessfully() {
        Snackbar.make(parent, getString(R.string.book_sent_to_email), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayBookSendingError() {
        Snackbar.make(parent, getString(R.string.error_sending_book), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displaySendingBookToEmail(boolean isSending) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (isSending) {
            progressDialog = dialogFactory.createProgressDialog(R.string.sending_book_to_email);
            progressDialog.show();
        }
    }
}
