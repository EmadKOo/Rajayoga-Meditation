package emad.youtube;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import emad.youtube.Adapters.DailyAdapter;
import emad.youtube.Adapters.DisplayingPlaylistsAdapter;
import emad.youtube.Model.Daily.Daily;
import emad.youtube.Model.PlayLists.PlayList;
import emad.youtube.VolleyUtils.MySingleton;

public class DailyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList allPhotos = new ArrayList();
    RecyclerView instagramRecycler;
    DailyAdapter adapter;
    Intent intent;
    FirebaseAuth mAuth;
    private static final String TAG = "DailyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        mAuth = FirebaseAuth.getInstance();
        initRecyclerView();
        getDaily();

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mAuth.getCurrentUser()==null)
            navigationView.getMenu().getItem(8).setTitle("Login ");
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

    public void getDaily(){
        String url = "https://api.instagram.com/v1/users/self/media/recent?access_token=10169293354.1677ed0.b1fc3fca0c9849fb9d9627cc6f08fdd0&count=250";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            Log.d(TAG, "onResponse: " + jsonData.length());
                            for (int x = 0; x <jsonData.length(); x++) {
                                if (jsonData.getJSONObject(x).get("type").equals("image")){
                                    allPhotos.add(jsonData.getJSONObject(x).getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse:  " + allPhotos.size());
                        }catch (Exception ex){
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: "+error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(DailyActivity.this).addToRequestQueue(stringRequest);
    }

    public void initRecyclerView(){
        instagramRecycler = findViewById(R.id.instagramRecycler);
        instagramRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DailyAdapter(DailyActivity.this,allPhotos);
        instagramRecycler.setAdapter(adapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(DailyActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            intent = new Intent(DailyActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type","allPlaylists");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_latestVideos) {
            intent = new Intent(DailyActivity.this, LatestVideosActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_Daily) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_Fav) {
            startActivity(new Intent(DailyActivity.this, FavouriteActivity.class));
            finish();
        } else if (id == R.id.nav_About) {
            startActivity(new Intent(DailyActivity.this, AboutUsActivity.class));
            finish();
        } else if (id == R.id.nav_Contact) {
            startActivity(new Intent(DailyActivity.this, ContactUsActivity.class));
            finish();
        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(DailyActivity.this, AuthActivity.class));
                finish();
            } else {
                mAuth.signOut();
                item.setTitle("Login ");
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
