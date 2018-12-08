package ba.lukic.petar.eknjiznica.ui.categories;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import ba.lukic.petar.eknjiznica.ui.category_books.CategoryBooksActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


public class CategoriesFragment extends DaggerFragment implements CategoriesAdapter.ICategoryInteraction, CategoriesContract.View {


    @BindView(R.id.rv_most_popular_categories)
    RecyclerView rvMostPopularCategories;
    @BindView(R.id.rv_all_categories)
    RecyclerView rvAllCategories;
    Unbinder unbinder;
    @BindView(R.id.swipe_refresh_parent)
    SwipeRefreshLayout swipeRefreshParent;


    private CategoriesAdapter mostPopularCategories;
    private CategoriesAdapter allCategories;

    @Inject
    CategoriesContract.Presenter presenter;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);

        mostPopularCategories = new CategoriesAdapter(this);
        allCategories = new CategoriesAdapter(this);

        rvAllCategories.setAdapter(allCategories);
        rvMostPopularCategories.setAdapter(mostPopularCategories);

        rvAllCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMostPopularCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter.takeView(this);
        presenter.onStart();
        presenter.loadCategories();
        swipeRefreshParent.setOnRefreshListener(() -> presenter.loadCategories());
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        presenter.onStop();
        presenter.dropView();

    }

    @Override
    public void onCategorySelected(CategoryVM categoryVM) {
        Context context = getActivity();
        if (context == null)
            return;

        context.startActivity(CategoryBooksActivity.GetInstance(categoryVM, getContext()));
    }

    @Override
    public void displayNoMostPopularCategories() {
        rvMostPopularCategories.setVisibility(View.GONE);
    }

    @Override
    public void displayNoCategories() {
        rvAllCategories.setVisibility(View.GONE);
    }

    @Override
    public void displayMostPopularCategories(List<CategoryVM> categoryVMList) {
        rvMostPopularCategories.setVisibility(View.VISIBLE);
        mostPopularCategories.updateData(categoryVMList);
    }

    @Override
    public void displayCategories(List<CategoryVM> categoryVMList) {
        rvAllCategories.setVisibility(View.VISIBLE);
        allCategories.updateData(categoryVMList);
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(swipeRefreshParent, R.string.msg_unexpected_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshParent.setRefreshing(isLoading);
    }
}
