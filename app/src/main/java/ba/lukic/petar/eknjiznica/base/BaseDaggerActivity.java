package ba.lukic.petar.eknjiznica.base;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import dagger.android.support.DaggerAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseDaggerActivity extends DaggerAppCompatActivity {




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void setupToolbar(Toolbar toolbar, boolean displayBackNavigation, int title) {
        if (toolbar == null)
            return;

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && displayBackNavigation) {
            supportActionBar.setTitle(title);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setupToolbar(Toolbar toolbar, boolean displayBackNavigation, String title) {
        if (toolbar == null)
            return;

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && displayBackNavigation) {
            supportActionBar.setTitle(title);
//            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void hideKeyboard() {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
            if (imm != null)
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }


}
