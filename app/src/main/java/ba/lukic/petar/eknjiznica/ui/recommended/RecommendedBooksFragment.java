package ba.lukic.petar.eknjiznica.ui.recommended;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerFragment;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.book_details.BookDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecommendedBooksFragment extends BaseDaggerFragment implements RecommededBooksContract.View, RecommendedBooksAdapter.IRecommendedBooksCallback {


    @BindView(R.id.rv_most_read)
    RecyclerView rvMostRead;
    @BindView(R.id.rv_top_selling)
    RecyclerView rvTopSelling;
    Unbinder unbinder;

    @Inject
    RecommededBooksContract.Presenter presenter;
    @BindView(R.id.swipe_refresh_parent)
    SwipeRefreshLayout swipeRefreshLayout;


    RecommendedBooksAdapter topSellingAdapter;
    RecommendedBooksAdapter recommededAdapter;
    @BindView(R.id.pb_loading_recommended)
    ProgressBar pbLoadingRecommended;
    @BindView(R.id.pb_loading_top_seling)
    ProgressBar pbLoadingTopSeling;

    public static RecommendedBooksFragment newInstance() {
        return new RecommendedBooksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommended_books, container, false);
        unbinder = ButterKnife.bind(this, view);

        topSellingAdapter = new RecommendedBooksAdapter(presenter, Glide.with(this), this, getResources());
        recommededAdapter = new RecommendedBooksAdapter(presenter, Glide.with(this), this, getResources());

        rvMostRead.setAdapter(recommededAdapter);
        rvTopSelling.setAdapter(topSellingAdapter);

        rvMostRead.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTopSelling.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter.takeView(this);
        presenter.onStart();
        presenter.loadBooks();
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadBooks());

        EventBus.getDefault().register(this);
        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onStop();
        presenter.dropView();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void displayNoRecommededBooks() {
        Snackbar.make(swipeRefreshLayout, R.string.no_recommended_books, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayNoTopSellingBooks() {
        Snackbar.make(swipeRefreshLayout, R.string.no_top_selling_books, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void displayTopSellingBooks(List<BookOfferVM> bookOfferVMList) {
        topSellingAdapter.updateData(bookOfferVMList);
    }

    @Override
    public void displayRecommendedBooks(List<BookOfferVM> bookOfferVMList) {
        recommededAdapter.updateData(bookOfferVMList);
    }

    @Override
    public void displayLoadingRecommended(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
        pbLoadingRecommended.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayLoadingTopSelling(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
        pbLoadingTopSeling.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(swipeRefreshLayout, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {

    }


    @Override
    public void onOpenBookDetails(BookOfferVM bookOfferVM) {
        startActivity(BookDetailsActivity.GetInstance(bookOfferVM, getContext()));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BookOfferVM bookOfferVM) {
        boolean isFavorite = presenter.isInFavorite(bookOfferVM);

        Snackbar make;
        if (isFavorite) {
            make = Snackbar.make(swipeRefreshLayout, String.format(getString(R.string.book_favorite_added), bookOfferVM.Title), Snackbar.LENGTH_LONG);
        } else {
            make = Snackbar.make(swipeRefreshLayout, String.format(getString(R.string.book_favorite_removed), bookOfferVM.Title), Snackbar.LENGTH_LONG);
        }
        make.setAction(R.string.undo, view -> {
            presenter.onFavoriteToggle(bookOfferVM);
        });
        make.show();
    }


}
