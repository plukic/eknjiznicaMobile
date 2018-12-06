package ba.lukic.petar.eknjiznica.ui.categories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<CategoryVM> categoryVMList = new ArrayList<>();
    private ICategoryInteraction callback;

    public CategoriesAdapter(ICategoryInteraction callback) {
        this.callback = callback;
    }

    public interface ICategoryInteraction {
        void onCategorySelected(CategoryVM categoryVM);
    }

    public void updateData(List<CategoryVM> categoryVMS){
        this.categoryVMList.clear();
        this.categoryVMList.addAll(categoryVMS);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_category, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).bind(categoryVMList.get(i));
    }

    @Override
    public int getItemCount() {
        return categoryVMList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;

        ViewHolder(View view) {
            super((view));
            ButterKnife.bind(this, view);
        }

        public void bind(CategoryVM categoryVM) {
            tvCategoryName.setText(categoryVM.CategoryName);
        }
    }
}
