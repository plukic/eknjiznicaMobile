package ba.lukic.petar.eknjiznica.ui.category_books;

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
import ba.lukic.petar.eknjiznica.model.book.ClientBookVM;
import ba.lukic.petar.eknjiznica.ui.books.BooksContract;
import ba.lukic.petar.eknjiznica.util.DateAndTimeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookOfferVM> bookOfferVMS = new ArrayList<>();
    private RequestManager requestManager;
    private ICategoryBooksCallback   callback;
    private BooksContract.Presenter presenter;

    public interface ICategoryBooksCallback {
        void onOpenBookDetails(BookOfferVM clientBookVM);
    }

    public CategoryBooksAdapter(RequestManager requestManager, ICategoryBooksCallback callback, BooksContract.Presenter presenter) {
        this.requestManager = requestManager;
        this.callback = callback;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_my_book, viewGroup, false);
        return new ViewHolder(inflate, requestManager);
    }

    public void favoriteBookTogle(FavoriteBookToggleEvent bookOfferVM) {
        for (int i = 0; i < bookOfferVMS.size(); i++) {
            if (bookOfferVM.data.BookId == bookOfferVMS.get(i).BookId) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bind(bookOfferVMS.get(i));
    }

    @Override
    public int getItemCount() {
        return bookOfferVMS.size();
    }

    void updateData(List<BookOfferVM> newData) {
        bookOfferVMS.clear();
        bookOfferVMS.addAll(newData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RequestManager requestManager;
        @BindView(R.id.book_image)
        ImageView bookImage;
        @BindView(R.id.btn_favorite)
        ImageButton btnFavorite;
        @BindView(R.id.tv_book_price)
        TextView tvBookPrice;
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_buy_date)
        TextView tvBuyDate;
        @BindView(R.id.cv_parent)
        CardView cvParent;
        private BookOfferVM bookOfferVM;

        public ViewHolder(View view, RequestManager requestManager) {
            super(view);
            this.requestManager = requestManager;
            ButterKnife.bind(this, view);
            cvParent.setOnClickListener(this);
            btnFavorite.setOnClickListener(this);
        }

        private void changeIcon(boolean isInFavorite) {
            if (isInFavorite)
                btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


        public void bind(BookOfferVM clientBookVm) {
            this.bookOfferVM = clientBookVm;
            int white = android.R.color.white;
            requestManager.load(clientBookVm.ImageUrl).apply(new RequestOptions().error(white).placeholder(white)).into(bookImage);
            tvBookPrice.setText(String.format("%sKM", clientBookVm.Price));
            tvBookTitle.setText(clientBookVm.Title);
            tvAuthor.setText(clientBookVm.AuthorName);
            tvBuyDate.setText("");
            boolean inFavorite = presenter.isInFavorite(clientBookVm);
            changeIcon(inFavorite);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_favorite) {
                presenter.onFavoriteToggle(bookOfferVM);
            } else {
                callback.onOpenBookDetails(bookOfferVM);
            }
        }
    }
}
