package ba.lukic.petar.eknjiznica.ui.my_books;

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

public class MyBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ClientBookVM> clientBookVMS = new ArrayList<>();
    private RequestManager requestManager;
    private DateAndTimeUtil dateAndTimeUtil;
    private IMyBooksCallback  callback;
    private BooksContract.Presenter presenter;
    private Resources resource;

    public interface IMyBooksCallback {
        void onOpenBookDetails(ClientBookVM clientBookVM);
    }

    public MyBooksAdapter(RequestManager requestManager, DateAndTimeUtil dateAndTimeUtil, IMyBooksCallback  callback, BooksContract.Presenter presenter, Resources resource) {
        this.requestManager = requestManager;
        this.dateAndTimeUtil = dateAndTimeUtil;
        this.callback = callback;
        this.presenter = presenter;
        this.resource = resource;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_my_book, viewGroup, false);
        return new ViewHolder(inflate, requestManager, dateAndTimeUtil);
    }

    public void favoriteBookTogle(FavoriteBookToggleEvent bookOfferVM) {
        for (int i = 0; i < clientBookVMS.size(); i++) {
            if (bookOfferVM.data.BookId == clientBookVMS.get(i).BookId) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bind(clientBookVMS.get(i));
    }

    @Override
    public int getItemCount() {
        return clientBookVMS.size();
    }

    void updateData(List<ClientBookVM> newData) {
        clientBookVMS.clear();
        clientBookVMS.addAll(newData);
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
        @BindView(R.id.tv_buy_date)
        TextView tvBuyDate;
        @BindView(R.id.cv_parent)
        CardView cvParent;
        private ClientBookVM bookOfferVM;

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


        public void bind(ClientBookVM clientBookVm) {
            this.bookOfferVM = clientBookVm;
            int white = android.R.color.white;
            requestManager.load(clientBookVm.FullImageUrl).apply(new RequestOptions().error(white).placeholder(white)).into(bookImage);
            tvBookPrice.setText(String.format("%sKM", clientBookVm.Price));
            tvBookTitle.setText(clientBookVm.BookTitle);
            tvAuthor.setText(clientBookVm.AuthorName);
            if (clientBookVm.BuyDate != null)
                tvBuyDate.setText(String.format(resource.getString(R.string.buy_date), dateAndTimeUtil.FormatForAddedToFavorites(clientBookVm.BuyDate)));
            else
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
