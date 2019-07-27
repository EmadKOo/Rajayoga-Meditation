package emad.youtube;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import emad.youtube.AuthActivity;
import emad.youtube.Fragments.AboutFragment;
import emad.youtube.Fragments.AllPlaylistsFragment;
import emad.youtube.Fragments.ContactUsFragment;
import emad.youtube.Fragments.DailyFragment;
import emad.youtube.Fragments.FavouriteVideosFragment;
import emad.youtube.Fragments.HomeFragment;
import emad.youtube.Fragments.LatestVideosFragment;
import emad.youtube.R;
import emad.youtube.SearchActivity;

public class NotificationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout fragmentFrameLayout;
    Fragment currentFragment;
    Toolbar toolbar;
    TextView toolbarTitle;
    ImageView goToSearch;
    Bundle bundle;
    FirebaseAuth mAuth;

    Intent intent;
    ActionBarDrawerToggle toggle = null;
    DrawerLayout drawer = null;
    boolean doubleBackToExitPressedOnce = false;

    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth= FirebaseAuth.getInstance();

        setupViews();

        currentFragment = new LatestVideosFragment();
        loadFragment(currentFragment);
        changeToolbar(new ColorDrawable(0xff009fce), "Latest Videos");
        bundle = new Bundle();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mAuth.getCurrentUser()==null)
            navigationView.getMenu().getItem(9).setTitle("Sign in ");

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        changeIcon(toggle, drawer,R.drawable.white_menu);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment instanceof HomeFragment){
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    //System.exit(0);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }else {
                loadFragment(new HomeFragment());
                changeToolbar(new ColorDrawable(0xff009fce), "Rajayoga Meditation");
                toolbarTitle.setTextColor(Color.parseColor("#ffffff"));
                changeIcon(toggle, drawer,R.drawable.white_menu);
            }
        }
    }

    public void changeIcon(ActionBarDrawerToggle mDrawerToggle, final DrawerLayout mDrawerLayout, int icon){
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), icon, getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public void setupViews(){
        fragmentFrameLayout = findViewById(R.id.fragmentFrameLayout);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        goToSearch = findViewById(R.id.goToSearch);
        goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsActivity.this, SearchActivity.class));
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            currentFragment = new HomeFragment();
            goToSearch.setVisibility(View.VISIBLE);
            changeToolbar(new ColorDrawable(0xff009fce), "Rajayoga Meditation");
            changeIcon(toggle, drawer,R.drawable.white_menu);
            toolbarTitle.setTextColor(Color.parseColor("#ffffff"));

        } else if (id == R.id.nav_allPlaylists) {
            currentFragment = new AllPlaylistsFragment();
            changeToolbar(new ColorDrawable(0xffffffff), "All Playlists");
            toolbarTitle.setTextColor(Color.parseColor("#ECA250"));
            changeIcon(toggle, drawer,R.drawable.yello_menu);
            bundle = new Bundle();
            bundle.putString("type", "allPlaylists");
            currentFragment.setArguments(bundle);

            goToSearch.setVisibility(View.GONE);
        } else if (id == R.id.nav_latestVideos) {
            currentFragment = new LatestVideosFragment();
            changeToolbar(new ColorDrawable(0xffffffff), "Latest Videos");
            toolbarTitle.setTextColor(Color.parseColor("#ECA250"));
            changeIcon(toggle, drawer,R.drawable.yello_menu);
            goToSearch.setVisibility(View.GONE);

        } else if (id == R.id.nav_Daily) {
            currentFragment = new DailyFragment();
            changeToolbar(new ColorDrawable(0xffffffff), "Daily Inspirations");
            toolbarTitle.setTextColor(Color.parseColor("#ECA250"));
            changeIcon(toggle, drawer,R.drawable.yello_menu);
            goToSearch.setVisibility(View.GONE);

        } else if (id == R.id.nav_Fav) {
            currentFragment = new FavouriteVideosFragment();
            changeToolbar(new ColorDrawable(0xffffffff), "Favourite");
            goToSearch.setVisibility(View.GONE);
            toolbarTitle.setTextColor(Color.parseColor("#ECA250"));
            changeIcon(toggle, drawer,R.drawable.yello_menu);
        } else if (id == R.id.navOffline) {
//            currentFragment = new OfflineFragment();
//            changeToolbar(new ColorDrawable(0xff00ADBC), "Downloaded Videos");
//            goToSearch.setVisibility(View.GONE);

//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri uri = Uri.par/home/emad/Desktop/splas.jpgse(Environment.DIRECTORY_DOWNLOADS+"/Rajayoga Meditation"); // a directory
//            intent.setDataAndType(uri, "*/*");
//            startActivity(Intent.createChooser(intent, "Display Offline Videos"));

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Rajayoga Meditation"); //  directory path
            intent.setDataAndType(uri, "*/mp4");
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 503);
        } else if (id == R.id.nav_About) {
            currentFragment = new AboutFragment();
            changeToolbar(new ColorDrawable(0xff009fce), "About US");
            toolbarTitle.setTextColor(Color.parseColor("#ffffff"));
            changeIcon(toggle, drawer,R.drawable.white_menu);
            goToSearch.setVisibility(View.GONE);
        } else if (id == R.id.nav_Contact) {
            currentFragment = new ContactUsFragment();
            changeToolbar(new ColorDrawable(0xff009fce), "Contact US");
            toolbarTitle.setTextColor(Color.parseColor("#ffffff"));
            changeIcon(toggle, drawer,R.drawable.white_menu);
            goToSearch.setVisibility(View.GONE);
        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            Log.d(TAG, "onNavigationItemSelected: ");
            if (mAuth.getCurrentUser() == null){
                startActivity(new Intent(NotificationsActivity.this, AuthActivity.class));
            } else {
                mAuth.signOut();
                item.setTitle("Sign in ");
            }
        }
        loadFragment(currentFragment);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    public boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentFrameLayout, fragment)
//                    .commit();
//            currentFragment = fragment;
//            return true;
//        }
//        return false;
//    }

    public void loadFragment(android.support.v4.app.Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.enter_from_right);
        transaction.replace(R.id.fragmentFrameLayout, fragment);
        currentFragment = fragment;
        transaction.commit();
    }
    private void changeToolbar(ColorDrawable colorDrawable, String title){
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        toolbarTitle.setText(title);
    }

    public void setCurrentFragment(Fragment fragment,ColorDrawable colorDrawable, String title){
        currentFragment = fragment;
        changeToolbar(colorDrawable, title);

    }

}