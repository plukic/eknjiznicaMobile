package ba.lukic.petar.eknjiznica.ui.recommended;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.model.book.BookOfferVM;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    public interface IRecommendedBooksCallback {
        void onOpenBookDetails(BookOfferVM bookOfferVM);
    }

    private List<BookOfferVM> bookOfferVMList = new ArrayList<>();
    private RecommededBooksContract.Presenter presenter;
    private RequestManager requestManager;
    private IRecommendedBooksCallback callback;
    private Resources resources;

    public RecommendedBooksAdapter(RecommededBooksContract.Presenter presenter, RequestManager requestManager, IRecommendedBooksCallback callback, Resources resources) {
        this.presenter = presenter;
        this.requestManager = requestManager;
        this.callback = callback;
        this.resources = resources;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false);

        return new ViewHolder(inflate, requestManager);
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

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        EventBus.getDefault().register(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        EventBus.getDefault().unregister(holder);
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

        public void bind(BookOfferVM bookOfferVM) {
            this.bookOfferVM = bookOfferVM;
            tvBookPrice.setText(String.format("%s KM", bookOfferVM.Price));
            tvAuthor.setText(bookOfferVM.AuthorName);
            tvBookTitle.setText(bookOfferVM.Title);

            changeIcon(presenter.isInFavorite(bookOfferVM));

            requestManager.load(bookOfferVM.ImageUrl).apply(new RequestOptions().error(android.R.color.white).placeholder(android.R.color.white)).into(bookImage);

        }

        private void changeIcon(boolean isInFavorite) {
            if (isInFavorite)
                btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }



        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(BookOfferVM toggleBook) {
            if(bookOfferVM.BookId==toggleBook.BookId){
                boolean inFavorite = presenter.isInFavorite(toggleBook);
                changeIcon(inFavorite);
            }
        };

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
