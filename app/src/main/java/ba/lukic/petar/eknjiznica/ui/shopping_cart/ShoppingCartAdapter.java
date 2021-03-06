package ba.lukic.petar.eknjiznica.ui.shopping_cart;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.model.FavoriteBookToggleEvent;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;
import ba.lukic.petar.eknjiznica.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookOfferVM> bookOfferVMList = new ArrayList<>();
    private RequestManager requestManager;
    private IShoppingCartCallback callback;

    public interface IShoppingCartCallback {
        void onBookDelete(BookOfferVM bookOfferVM);
    }

    public ShoppingCartAdapter(RequestManager requestManager, IShoppingCartCallback callback) {
        this.requestManager = requestManager;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_shopping_cart_book, viewGroup, false);
        return new ViewHolder(inflate, requestManager);
    }

    public void favoriteBookTogle(FavoriteBookToggleEvent bookOfferVM) {
        for (int i = 0; i < bookOfferVMList.size(); i++) {
            if (bookOfferVM.data == bookOfferVMList.get(i)) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bind(bookOfferVMList.get(i));
    }

    @Override
    public int getItemCount() {
        return bookOfferVMList.size();
    }

    void updateData(List<BookOfferVM> newData) {
        bookOfferVMList.clear();
        bookOfferVMList.addAll(newData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RequestManager requestManager;
        @BindView(R.id.book_image)
        ImageView bookImage;
        @BindView(R.id.btn_delete)
        ImageButton btnDelete;
        @BindView(R.id.tv_book_price)
        TextView tvBookPrice;
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.cv_parent)
        CardView cvParent;
        private BookOfferVM bookOfferVM;

        public ViewHolder(View view, RequestManager requestManager) {
            super(view);
            this.requestManager = requestManager;
            ButterKnife.bind(this, view);
            btnDelete.setOnClickListener(this);
        }

        public void bind(BookOfferVM bookOfferVM) {
            this.bookOfferVM = bookOfferVM;
            int white = android.R.color.white;
            requestManager.load(bookOfferVM.ImageUrl).apply(new RequestOptions().error(white).placeholder(white)).into(bookImage);
            tvBookPrice.setText(String.format("%sKM", bookOfferVM.Price));
            tvBookTitle.setText(bookOfferVM.Title);
            tvAuthor.setText(bookOfferVM.AuthorName);
        }

        @Override
        public void onClick(View view) {
            callback.onBookDelete(bookOfferVM);
        }
    }
}
