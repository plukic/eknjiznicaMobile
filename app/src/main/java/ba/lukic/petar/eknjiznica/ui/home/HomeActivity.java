package ba.lukic.petar.eknjiznica.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ba.lukic.petar.eknjiznica.R;
import ba.lukic.petar.eknjiznica.base.BaseDaggerAuthorizedActivity;
import ba.lukic.petar.eknjiznica.data.account.IAccountRepo;
import ba.lukic.petar.eknjiznica.ui.categories.CategoriesFragment;
import ba.lukic.petar.eknjiznica.ui.favorite_books.FavoriteBooksFragment;
import ba.lukic.petar.eknjiznica.ui.profile.ProfileActivity;
import ba.lukic.petar.eknjiznica.ui.recommended.RecommendedBooksFragment;
import ba.lukic.petar.eknjiznica.util.DialogFactory;
import ba.lukic.petar.eknjiznica.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class HomeActivity extends BaseDaggerAuthorizedActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @Inject
    IAccountRepo accountRepo;
    @Inject
    ISchedulersProvider schedulersProvider;
    @Inject
    DialogFactory dialogFactory;

    private TextView tvFullName;
    private TextView tvEmail;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.title_activity_home);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        List<Fragment> fragments = Arrays.asList(RecommendedBooksFragment.newInstance(), CategoriesFragment.newInstance(), FavoriteBooksFragment.GetInstance());
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

        // Set up the ViewPager with the sections adapter.
        container.setAdapter(mSectionsPagerAdapter);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));


        setupNavHeader();


        navView.setNavigationItemSelectedListener(menuItem -> {
                    drawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_books:
                            return true;

                        case R.id.nav_logout:
                            onLogoutAction();
                            return true;
                        case R.id.nav_profile:
                            startActivity(ProfileActivity.getInstance(HomeActivity.this));
                            return true;
                    }
                    return false;
                }
        );

    }

    private void onLogoutAction() {
        dialogFactory.createCancelOkDialog(R.string.title_logout, R.string.title_logout_message, (dialogInterface, i) -> {
            HomeActivity.this.logoutUser(null);
            dialogInterface.dismiss();
        }, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }).show();

    }

    private void setupNavHeader() {
        View headerView = navView.getHeaderView(0);
        tvFullName = headerView.findViewById(R.id.tv_full_name);
        tvEmail = headerView.findViewById(R.id.tv_email);


        Disposable subscribe = accountRepo.loadUserProfileInfo()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(clientsDetailsModel -> {
                    tvFullName.setText(String.format("%s %s", clientsDetailsModel.FirstName, clientsDetailsModel.LastName));
                    tvEmail.setText(clientsDetailsModel.Email);
                }, Timber::e);

        compositeDisposable.add(subscribe);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getInstance(Context ctx) {
        return new Intent(ctx, HomeActivity.class);
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }
    }
}
