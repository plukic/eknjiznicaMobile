package ba.lukic.petar.eknjiznica.ui.favorite_books;

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

public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookOfferVM> bookOfferVMList = new ArrayList<>();
    private RequestManager requestManager;
    private DateAndTimeUtil dateAndTimeUtil;
    private IBooksCallback callback;
    private BooksContract.Presenter presenter;
    private Resources resource;

    public interface IBooksCallback {
        void onOpenBookDetails(BookOfferVM bookOfferVM);
    }

    public BooksAdapter(RequestManager requestManager, DateAndTimeUtil dateAndTimeUtil, IBooksCallback callback, BooksContract.Presenter presenter, Resources resource) {
        this.requestManager = requestManager;
        this.dateAndTimeUtil = dateAndTimeUtil;
        this.callback = callback;
        this.presenter = presenter;
        this.resource = resource;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_favorite_book, viewGroup, false);
        return new ViewHolder(inflate, requestManager, dateAndTimeUtil);
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
        private final DateAndTimeUtil dateAndTimeUtil;
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
        @BindView(R.id.tv_added_to_favorites)
        TextView tvAddedToFavorites;
        @BindView(R.id.cv_parent)
        CardView cvParent;
        private BookOfferVM bookOfferVM;

        public ViewHolder(View view, RequestManager requestManager, DateAndTimeUtil dateAndTimeUtil) {
            super(view);
            this.requestManager = requestManager;
            this.dateAndTimeUtil = dateAndTimeUtil;
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


        public void bind(BookOfferVM bookOfferVM) {
            this.bookOfferVM = bookOfferVM;
            int white = android.R.color.white;
            requestManager.load(bookOfferVM.ImageUrl).apply(new RequestOptions().error(white).placeholder(white)).into(bookImage);
            tvBookPrice.setText(String.format("%sKM", bookOfferVM.Price));
            tvBookTitle.setText(bookOfferVM.Title);
            tvAuthor.setText(bookOfferVM.AuthorName);
            if (bookOfferVM.AddedToFavorites != null)
                tvAddedToFavorites.setText(String.format(resource.getString(R.string.added_to_favorites), dateAndTimeUtil.FormatForAddedToFavorites(bookOfferVM.AddedToFavorites)));
            else
                tvAddedToFavorites.setText("");
            boolean inFavorite = presenter.isInFavorite(bookOfferVM);
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
