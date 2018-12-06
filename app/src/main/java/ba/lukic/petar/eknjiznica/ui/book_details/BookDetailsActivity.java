package ba.lukic.petar.eknjiznica.ui.book_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import butterknife.BindView;
import butterknife.ButterKnife;


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
    private BookOfferVM book;

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

        Glide.with(this).load(book.ImageUrl).apply(new RequestOptions().error(android.R.color.white).placeholder(android.R.color.white)).into(bookImage);
        presenter.takeView(this);
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
            presenter.toggleFavorite(book);
            eventBus.post(new FavoriteBookToggleEvent(book, false));
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

        boolean inFavorite = presenter.isInFavorite(book.BookId);
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
            presenter.toggleFavorite(book);
        });
        make.show();
    }
}
