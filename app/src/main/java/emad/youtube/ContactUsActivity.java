package emad.youtube;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class ContactUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView contactImage;
    TextView contactChannelName;
    TextView contactChannelDesc;
    TextView mailText;
    RelativeLayout emailLayout;
    ImageView faceLayout;
    ImageView youtubeLayout;
    ImageView instaLayout;
    Intent intent;

    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    FirebaseAuth mAuth;
    private static final String TAG = "ContactUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        currentAPIKey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();
        initViews();
        handleViews();
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

    public void initViews(){
        contactImage = findViewById(R.id.contactImage);
        mailText = findViewById(R.id.mailText);
        contactChannelName = findViewById(R.id.contactChannelName);
        contactChannelDesc = findViewById(R.id.contactChannelDesc);
        emailLayout = findViewById(R.id.emailLayout);
        faceLayout = findViewById(R.id.faceLayout);
        instaLayout = findViewById(R.id.instaLayout);
        youtubeLayout = findViewById(R.id.youtubeLayout);
    }

    public void handleViews(){
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "erajayogameditationonline@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android");
                startActivity(Intent.createChooser(intent, "Send Me Email"));
            }
        });

        youtubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/channel/UCAdN7ghumPNFA7Ww3vf_KpQ/featured"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/channel/UCAdN7ghumPNFA7Ww3vf_KpQ/featured"));
                    startActivity(intent);
                }
            }
        });

        instaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/RajayogaMeditation");
                intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setPackage("com.instagram.android");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/RajayogaMeditation")));
                }
            }
        });

        faceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = newFacebookIntent(getPackageManager(), "https://www.facebook.com/rajayogameditationonline");
                startActivity(intent);
            }
        });

    }
    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
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
            startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            intent = new Intent(ContactUsActivity.this, ViewPlayListsActivity.class);
            intent.putExtra("type","allPlaylists");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_latestVideos) {
            startActivity(intent = new Intent(ContactUsActivity.this, LatestVideosActivity.class));
            finish();
        } else if (id == R.id.nav_Daily) {
            startActivity(intent = new Intent(ContactUsActivity.this, DailyActivity.class));
            finish();
        } else if (id == R.id.nav_Fav) {
            startActivity(intent = new Intent(ContactUsActivity.this, FavouriteActivity.class));
            finish();
        } else if (id == R.id.nav_About) {
            if (mAuth.getCurrentUser() == null){
            startActivity(intent = new Intent(ContactUsActivity.this, AboutUsActivity.class));
                finish();
            } else {
                mAuth.signOut();
                item.setTitle("Login ");
            }
        }
        else if (id == R.id.nav_Contact) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            startActivity(intent = new Intent(ContactUsActivity.this, AuthActivity.class));
            finish();
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
                                Picasso.get().load(jsonData.getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url")).placeholder(R.drawable.placeholder).into(contactImage);
                            }catch (Exception ex){
                                Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(contactImage);
                            }

                            contactChannelName.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("title"));
//                            mailText.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("title"));

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

        MySingleton.getmInsance(ContactUsActivity.this).addToRequestQueue(stringRequest);

    }
}
