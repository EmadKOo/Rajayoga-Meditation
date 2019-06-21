package emad.youtube;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import emad.youtube.Adapters.DailyAdapter;
import emad.youtube.Adapters.FavouriteAdapter;
import emad.youtube.Model.Daily.Data;
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Tools.RegularFont;

public class FavouriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView favRecycler;
    ArrayList<Favourite> favouriteList = new ArrayList<>();
    FavouriteAdapter adapter;
    RegularFont noFav;

    FirebaseAuth mAuth;
    DatabaseReference favRef;
    Intent intent;
    private static final String TAG = "FavouriteActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        noFav = findViewById(R.id.noFav);
        initRecyclerView();
        mAuth = FirebaseAuth.getInstance();
        getFavouriteListFromFirebase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mAuth.getCurrentUser()==null)
            navigationView.getMenu().getItem(8).setTitle("Sign in ");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        changeIcon(toggle, drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void changeIcon(ActionBarDrawerToggle mDrawerToggle, final DrawerLayout mDrawerLayout){
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.yello_menu, getTheme());
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            intent = new Intent(FavouriteActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type", "allPlaylists");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_latestVideos) {
            intent = new Intent(FavouriteActivity.this, LatestVideosActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_Daily) {
            startActivity(new Intent(FavouriteActivity.this, DailyActivity.class));
            finish();
        } else if (id == R.id.nav_Fav) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_About) {
            startActivity(new Intent(FavouriteActivity.this, AboutUsActivity.class));
            finish();
        } else if (id == R.id.nav_Contact) {
            startActivity(new Intent(FavouriteActivity.this, ContactUsActivity.class));
            finish();
        } else if (id == R.id.nav_Rate) {

        } else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser() == null){
                startActivity(new Intent(FavouriteActivity.this, LoginActivity.class));
                finish();
            } else {
                mAuth.signOut();
                item.setTitle("Sign in ");
            }
    }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initRecyclerView(){
        favRecycler = findViewById(R.id.favRecycler);
        favRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouriteAdapter(favouriteList, FavouriteActivity.this);
        favRecycler.setAdapter(adapter);
    }

    public void getFavouriteListFromFirebase(){
        if (mAuth.getCurrentUser()==null){
            noFav.setVisibility(View.VISIBLE);
            noFav.setText("Login To See Your Favourite Videos");
            favRecycler.setVisibility(View.GONE);
        }else {
            favRef = FirebaseDatabase.getInstance().getReference().child("fav").child(mAuth.getCurrentUser().getUid());
            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favouriteList.clear();
                    Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        favouriteList.add(snapshot.getValue(Favourite.class));
                    }

                    Log.d(TAG, "onDataChange: " + favouriteList.size());
                    if (favouriteList.size()==0)
                        noFav.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
