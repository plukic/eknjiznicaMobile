package ba.lukic.petar.eknjiznica.ui.categories;

import java.util.List;

import ba.lukic.petar.eknjiznica.base.BaseAsyncView;
import ba.lukic.petar.eknjiznica.base.BasePresenter;
import ba.lukic.petar.eknjiznica.model.category.CategoryVM;

public interface CategoriesContract {

    interface View extends BaseAsyncView<Presenter>{
        void displayNoMostPopularCategories();
        void  displayNoCategories();

        void displayMostPopularCategories(List<CategoryVM> categoryVMList);
        void  displayCategories(List<CategoryVM> categoryVMList);

    }

    interface Presenter extends BasePresenter<View> {
        void loadCategories();
    }

}
