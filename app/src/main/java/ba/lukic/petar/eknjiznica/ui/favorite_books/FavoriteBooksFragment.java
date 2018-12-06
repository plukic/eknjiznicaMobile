package ba.lukic.petar.eknjiznica.ui.favorite_books;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.book_details.BookDetailsActivity;
import ba.lukic.petar.eknjiznica.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteBooksFragment extends DaggerFragment implements FavoriteBooksContract.View ,BooksAdapter.IBooksCallback{


    public static FavoriteBooksFragment GetInstance(){
        return new FavoriteBooksFragment();
    }
    @Inject
    FavoriteBooksContract.Presenter presenter;
    @BindView(R.id.rv_book)
    RecyclerView rvBook;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;
    private View parent;
    @Inject
    DateAndTimeUtil dateAndTimeUtil;
    @Inject
    EventBus eventBus;
private BooksAdapter booksAdapter;
    public FavoriteBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_favorite_books, container, false);
        unbinder = ButterKnife.bind(this, parent);

        booksAdapter=new BooksAdapter(Glide.with(this),dateAndTimeUtil,this,presenter);
        rvBook.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBook.setAdapter(booksAdapter);
        eventBus.register(this);

        presenter.takeView(this);
        presenter.onStart();
        presenter.loadFavoriteBooks();
        swipeRefresh.setOnRefreshListener(() -> presenter.loadFavoriteBooks());
        return parent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        eventBus.unregister(this);
        presenter.dropView();
        presenter.onStop();
    }

    @Override
    public void displayNoFavoriteBooks() {
        rvBook.setVisibility(View.GONE);
        Snackbar.make(parent, R.string.no_favorite_books, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayFavoriteBooks(List<BookOfferVM> favoriteBook) {
        rvBook.setVisibility(View.VISIBLE);
        booksAdapter.updateData(favoriteBook);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefresh.setRefreshing(isLoading);
    }

    @Override
    public void onOpenBookDetails(BookOfferVM bookOfferVM) {
        startActivity(BookDetailsActivity.GetInstance(bookOfferVM, getContext()));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FavoriteBookToggleEvent bookOfferVM) {
        presenter.loadFavoriteBooks();
        if(!bookOfferVM.displayNotification)
            return;

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
