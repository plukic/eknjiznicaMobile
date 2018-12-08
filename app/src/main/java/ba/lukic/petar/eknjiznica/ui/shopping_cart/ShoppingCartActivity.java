package ba.lukic.petar.eknjiznica.ui.shopping_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartActivity extends BaseDaggerAuthorizedActivity implements ShoppingCartContract.View, ShoppingCartAdapter.IShoppingCartCallback {

    @Inject
    ShoppingCartContract.Presenter presenter;
    @Inject
    DialogFactory dialogFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_shopping_cart_items)
    RecyclerView rvShoppingCartItems;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.imbToggleBottomSheet)
    ImageButton imbToggleBottomSheet;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_tax)
    TextView tvTax;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.bottom_sheet_shopping_cart)
    ConstraintLayout bottomSheetShoppingCart;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    private ShoppingCartAdapter adapter;
    private ProgressDialog progressDialog;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetShoppingCartBehaviour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setupToolbar(toolbar, true, R.string.title_shopping_cart_activity);


        adapter = new ShoppingCartAdapter(Glide.with(this), this);
        rvShoppingCartItems.setAdapter(adapter);
        rvShoppingCartItems.setLayoutManager(new LinearLayoutManager(this));
        presenter.takeView(this);
        presenter.onStart();
        presenter.loadBooks();
        bottomSheetSetup();
    }

    private void bottomSheetSetup() {

        bottomSheetShoppingCartBehaviour = BottomSheetBehavior.from(bottomSheetShoppingCart);
        bottomSheetShoppingCartBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                updateFilterHeadersAlpha(slideOffset);
            }


        });
    }
    @OnClick(R.id.imbToggleBottomSheet)
    public void onToggle(){
        if (bottomSheetShoppingCartBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetShoppingCartBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else
            bottomSheetShoppingCartBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private void updateFilterHeadersAlpha(float slideOffset) {
        imbToggleBottomSheet.setRotation(slideOffset*-180);
    }
    @Override
    public void displayShoppingCartBooks(List<BookOfferVM> bookOfferVMList) {
        adapter.updateData(bookOfferVMList);
    }

    @Override
    public void displayPrice(double total, double price, double tax) {
        tvTotal.setText(String.format(getString(R.string.total_price), total));
        tvTax.setText(String.format(getString(R.string.tax), tax));
        tvPrice.setText(String.format(getString(R.string.price), price));
    }

    @Override
    public void toggleBuyButton(boolean isEnabled) {
        tvBuy.setEnabled(isEnabled);
    }

    @Override
    public void displayBooksSentToEmail() {
        Snackbar.make(parent,R.string.books_sent_to_email,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String errror) {
        Snackbar.make(parent,errror,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent,R.string.msg_unexpected_error,Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.tv_buy)
    public  void onBuy(){
        presenter.buyBooks();
    }
    @Override
    public void displayLoading(boolean isLoading) {
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
        if (isLoading) {
            progressDialog = dialogFactory.createProgressDialog(R.string.buying_book);
            progressDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookDelete(BookOfferVM bookOfferVM) {
        presenter.deleteBookFromShoppingCart(bookOfferVM);
    }

    public static Intent GetInstance(Context context){
        return new Intent(context,ShoppingCartActivity.class);
    }
}
