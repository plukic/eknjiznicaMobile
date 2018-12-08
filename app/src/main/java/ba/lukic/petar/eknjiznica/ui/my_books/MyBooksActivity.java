package ba.lukic.petar.eknjiznica.ui.my_books;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.book_details.BookDetailsActivity;
import ba.lukic.petar.eknjiznica.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBooksActivity extends BaseDaggerAuthorizedActivity implements MyBooksContract.View, MyBooksAdapter.IMyBooksCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_my_books)
    RecyclerView rvMyBooks;
    @BindView(R.id.swipe_refresh_parent)
    SwipeRefreshLayout swipeRefreshParent;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private SearchView searchView;


    @Inject
    MyBooksContract.Presenter presenter;

    @Inject
    DateAndTimeUtil dateAndTimeUtil;
    private MyBooksAdapter myBooksAdapter;
    private String query;

    public static Intent GetInstance(Context ctx) {
        return new Intent(ctx, MyBooksActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        ButterKnife.bind(this);
        setupToolbar(toolbar, true, R.string.title_my_books);


        myBooksAdapter = new MyBooksAdapter(Glide.with(this), dateAndTimeUtil, this, presenter, getResources());
        rvMyBooks.setLayoutManager(new LinearLayoutManager(this));
        rvMyBooks.setAdapter(myBooksAdapter);
        presenter.takeView(this);
        presenter.onStart();
        handleSearchIntent(getIntent());
        swipeRefreshParent.setOnRefreshListener(() -> presenter.loadBooks(query));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null)
                searchView.setQuery(query, false);
            presenter.loadBooks(query);
        } else {
            query = null;
            presenter.loadBooks(null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearchIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s == null || s.isEmpty()) {
                    query = null;
                    presenter.loadBooks(query);
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void displayMyBooks(List<ClientBookVM> clientBooks) {
        myBooksAdapter.updateData(clientBooks);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshParent.setRefreshing(isLoading);
    }

    @Override
    public void onOpenBookDetails(ClientBookVM clientBookVM) {
        startActivity(BookDetailsActivity.GetInstance(new BookOfferVM(clientBookVM), this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FavoriteBookToggleEvent bookOfferVM) {
        myBooksAdapter.favoriteBookTogle(bookOfferVM);
        boolean isFavorite = presenter.isInFavorite(bookOfferVM.data);
        Snackbar make;
        if (isFavorite) {
            make = Snackbar.make(parent, String.format(getString(R.string.book_favorite_added), bookOfferVM.data.Title), Snackbar.LENGTH_LONG);
        } else {
            make = Snackbar.make(parent, String.format(getString(R.string.book_favorite_removed), bookOfferVM.data.Title), Snackbar.LENGTH_LONG);
        }
        make.setAction(R.string.undo, view -> {
            presenter.onFavoriteToggle(bookOfferVM.data);
        });
        make.show();
    }
}
