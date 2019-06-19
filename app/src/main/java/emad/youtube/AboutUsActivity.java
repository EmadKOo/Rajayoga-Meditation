package emad.youtube;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import emad.youtube.Model.Comment.Comment;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Intent intent;
    CircleImageView imgAbout;
    TextView aboutChannelName;
    TextView aboutDesc;
    FirebaseAuth mAuth;
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    private static final String TAG = "AboutUsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        currentAPIKey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();
        setupViews();
        getChannelAbout();

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

    public void setupViews(){
        imgAbout = findViewById(R.id.imgAbout);
        aboutChannelName = findViewById(R.id.aboutChannelName);
        aboutDesc = findViewById(R.id.aboutDesc);
    }
    public void changeIcon(ActionBarDrawerToggle mDrawerToggle, final DrawerLayout mDrawerLayout){
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.white_menu, getTheme());
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
            startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            intent = new Intent(AboutUsActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type","allPlaylists");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_latestVideos) {
            startActivity(intent = new Intent(AboutUsActivity.this, LatestVideosActivity.class));
            finish();
        } else if (id == R.id.nav_Daily) {
            startActivity(intent = new Intent(AboutUsActivity.this, DailyActivity.class));
            finish();
        } else if (id == R.id.nav_Fav) {
            startActivity(intent = new Intent(AboutUsActivity.this, FavouriteActivity.class));
            finish();
        } else if (id == R.id.nav_About) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_Contact) {
            startActivity(intent = new Intent(AboutUsActivity.this, ContactUsActivity.class));
            finish();
        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser() == null){
            startActivity(intent = new Intent(AboutUsActivity.this, AuthActivity.class));
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


    public void getChannelAbout(){
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet&key="+ currentAPIKey +"&id=UCAdN7ghumPNFA7Ww3vf_KpQ";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");
                            try {
                                Picasso.get().load(jsonData.getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url")).placeholder(R.drawable.placeholder).into(imgAbout);
                            }catch (Exception ex){
                                Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(imgAbout);
                            }

                            aboutChannelName.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("title"));
                            aboutDesc.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("description"));

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

        MySingleton.getmInsance(AboutUsActivity.this).addToRequestQueue(stringRequest);

    }
}
