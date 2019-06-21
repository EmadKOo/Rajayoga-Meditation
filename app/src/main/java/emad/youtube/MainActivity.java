package emad.youtube;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import emad.youtube.Model.Latest.Latest;
import emad.youtube.Model.Video.Video;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        YouTubePlayer.OnInitializedListener
        ,YouTubePlayer.PlaybackEventListener
        ,YouTubePlayer.PlayerStateChangeListener {

//    YouTubePlayerView playerView;
    YouTubePlayer mYouTubePlayer;
    YouTubePlayerSupportFragment frag;
    String videoID;
    Video video;
    RelativeLayout dailyMurli;
    RelativeLayout murliRevision;
    RelativeLayout experienceMeditaion;
    RelativeLayout dailyInspiration;
    ImageView goToSearch;

    Intent intent;
    FirebaseAuth mAuth;
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


        currentAPIKey = randomAPI.getRandomKey();
        Log.d(TAG, "onCreate: APIIIIIII  " + currentAPIKey);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        initViews();
        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        playerView = findViewById(R.id.playerView);
        getLatestVideoID();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mAuth.getCurrentUser()==null)
            navigationView.getMenu().getItem(8).setTitle("Sign in ");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        changeIcon(toggle, drawer);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void initViews(){
        goToSearch = findViewById(R.id.goToSearch);
        dailyMurli = findViewById(R.id.dailyMurli);
        murliRevision= findViewById(R.id.murliRevision);
        experienceMeditaion= findViewById(R.id.experienceMeditaion);
        dailyInspiration= findViewById(R.id.dailyInspiration);

        dailyMurli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewsAction("type","dailyMurli","Shiv Baba Daily Murli ");
            }
        });

        murliRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewsAction("type","murliRevision","Avyakt Murli Revision");
            }
        });

        experienceMeditaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewsAction("type","experienceMeditaion","Experience Meditation");
            }
        });
        dailyInspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyActivity.class));
            }
        });

        goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
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

    public void viewsAction(String tag, String type, String name){
        intent = new Intent(MainActivity.this, ViewPlayListsActivity.class);
        intent.putExtra(tag, type);
        intent.putExtra("name", name);
        startActivity(intent);
    }
    // get The latest Video id of Creative Meditation
    private void getLatestVideoID() {
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?rel=0&part=snippet&maxResults=50&playlistId=PLkYTNBmHiJIirqEKHbB8Nd5gAutWaIGpz&key="+currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           video =new Gson().fromJson(response, Video.class);
                            int flagNOTPrivate = 0;
                                for (int x = 0; x <video.getItems().length ; x++) {
                                    if (!video.getItems()[x].getVideoSnippets().getTitle().equals("Private video")){
                                        flagNOTPrivate=1;
                                        videoID = video.getItems()[x].getVideoSnippets().getVideoResources().getVideoId();

                                    }
                                    if (flagNOTPrivate==1)
                                        break;
                            }

                            frag.initialize(currentAPIKey,MainActivity.this);

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

        MySingleton.getmInsance(MainActivity.this).addToRequestQueue(stringRequest);
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
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_allPlaylists) {
            viewsAction("type","allPlaylists","");
        } else if (id == R.id.nav_latestVideos) {
            intent = new Intent(MainActivity.this, LatestVideosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Daily) {
            startActivity(new Intent(MainActivity.this, DailyActivity.class));
        } else if (id == R.id.nav_Fav) {
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
        } else if (id == R.id.nav_About) {
            startActivity(intent = new Intent(MainActivity.this, AboutUsActivity.class));
        } else if (id == R.id.nav_Contact) {
            startActivity(intent = new Intent(MainActivity.this, ContactUsActivity.class));
        } else if (id == R.id.nav_Rate) {

        } else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser()==null)
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            else{
                mAuth.signOut();
                item.setTitle("Sign in ");
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;
        mYouTubePlayer.setPlayerStateChangeListener(this);
        mYouTubePlayer.setPlaybackEventListener(this);
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!b)
            mYouTubePlayer.cueVideo(videoID);


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPlaying() {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    }

    @Override
    public void onPaused() {
        Log.d(TAG, "onPaused: STOPPED");
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }

    @Override
    public void onStopped() {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }

    @Override
    public void onLoaded(String s) {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    }

    @Override
    public void onVideoEnded() {
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }
}
