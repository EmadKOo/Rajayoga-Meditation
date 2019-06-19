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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import emad.youtube.Adapters.DailyAdapter;
import emad.youtube.Adapters.LatestVideosAdapter;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class LatestVideosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView latestVideosRecycler;
    LatestVideosAdapter adapter;
    Latest latest = new Latest();
    ArrayList<Latest> latestVideos = new ArrayList<>();
    Intent intent;
    FirebaseAuth mAuth;

    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";

    private static final String TAG = "LatestVideosActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_videos);

        currentAPIKey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();
        initRecyclerView();
        getLatest();


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
    public void getLatest(){
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCAdN7ghumPNFA7Ww3vf_KpQ&maxResults=10&order=date&type=video&key="+ currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");
                            latestVideos.clear();
                            for (int x = 0; x <jsonData.length() ; x++) {
                                if (!jsonData.getJSONObject(x).getJSONObject("snippet").getString("title").equals("Private video")){

                                    latest = new Latest();

                                    latest.setVideoId(jsonData.getJSONObject(x).getJSONObject("id").getString("videoId"));
                                    latest.setPublishedAt(jsonData.getJSONObject(x).getJSONObject("snippet").getString("publishedAt"));
                                    latest.setTitle(jsonData.getJSONObject(x).getJSONObject("snippet").getString("title"));
                                    latest.setDescription(jsonData.getJSONObject(x).getJSONObject("snippet").getString("description"));
                                    latest.setUrl(jsonData.getJSONObject(x).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
                                    latest.setChannelTitle(jsonData.getJSONObject(x).getJSONObject("snippet").getString("channelTitle"));

                                    latestVideos.add(latest);
                                }
                            }
                            adapter.notifyDataSetChanged();

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

        MySingleton.getmInsance(LatestVideosActivity.this).addToRequestQueue(stringRequest);
    }

    public void initRecyclerView(){
        latestVideosRecycler = findViewById(R.id.latestVideosRecycler);
        latestVideosRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LatestVideosAdapter(latestVideos,LatestVideosActivity.this);
        latestVideosRecycler.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(LatestVideosActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            intent = new Intent(LatestVideosActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type","allPlaylists");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_latestVideos) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_Daily) {
            intent = new Intent(LatestVideosActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type","allPlaylists");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_Fav) {
            startActivity(new Intent(LatestVideosActivity.this, FavouriteActivity.class));
            finish();
        } else if (id == R.id.nav_About) {
            startActivity(new Intent(LatestVideosActivity.this, AboutUsActivity.class));
            finish();
        } else if (id == R.id.nav_Contact) {
            startActivity(new Intent(LatestVideosActivity.this, ContactUsActivity.class));
            finish();
        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(LatestVideosActivity.this, AuthActivity.class));
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