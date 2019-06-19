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
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;

import emad.youtube.Adapters.DisplayingPlaylistsAdapter;
import emad.youtube.Model.Daily.Daily;
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Model.PlayLists.PlayList;
import emad.youtube.Model.PlayLists.PlayListItems;
import emad.youtube.Model.Video.Video;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class ViewPlayListsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView selectedPlaylistsRecycler;
    DisplayingPlaylistsAdapter adapter;

    ArrayList playlistsIDList = new ArrayList();
    ArrayList<PlayListItems> mPlayLists = new ArrayList<>();
    PlayList playList = new PlayList();
    String playlistType = "";
    Intent intent;
    FirebaseAuth mAuth;
    String currentAPIkey ="";
    RandomAPI randomAPI = new RandomAPI();
    private static final String TAG = "ViewPlayListsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_playlists);

        currentAPIkey = randomAPI.getRandomKey();

        Toolbar toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        playlistType = getIntent().getStringExtra("type");
        initRecyclerView();

        if (!playlistType.equals("allPlaylists")){
            setUpIDs(playlistType);
            getAllPlayList(playlistsIDList);
            toolbar.setTitle(getIntent().getStringExtra("name"));
        }else if (playlistType.equals("allPlaylists")){
            getAllPlayList();
        }else {
            Log.d(TAG, "onCreate: some thing wrong");
        }
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
    public void setUpIDs(String type){
        if (type.equals("dailyMurli")){
            playlistsIDList.add("PLkYTNBmHiJIiLBmyy3YBFZLHFHodFP0K3");
            playlistsIDList.add("PLkYTNBmHiJIhPbBirn7ufe6h7zBGXFsAH");
            playlistsIDList.add("PLkYTNBmHiJIj-WlEFa-wvHScBJloTb3ah");
            playlistsIDList.add("PLkYTNBmHiJIiV-wY1dEdZSzwKXPlObzFL");
        }else if (type.equals("murliRevision")){
            playlistsIDList.add("PLkYTNBmHiJIi0J30Yzmel8LZMjYe1yX7I");
            playlistsIDList.add("PLkYTNBmHiJIgi_Zo0V_LLRl41Q831NOwd");
            playlistsIDList.add("PLkYTNBmHiJIgkzaAHpuMsLPfAW-wLoXm2");
        }else if (type.equals("experienceMeditaion")){
            playlistsIDList.add("PLkYTNBmHiJIirqEKHbB8Nd5gAutWaIGpz");
            playlistsIDList.add("PLkYTNBmHiJIgyRlFSpAhfFzkes8be8n5a");
            playlistsIDList.add("PLkYTNBmHiJIhz4m1EXxDXow80xsglA18V");
            playlistsIDList.add("PLkYTNBmHiJIjMuUW9b2PkApIV_Cv8auQ7");
            playlistsIDList.add("PLkYTNBmHiJIiaYcPdC-WDCZbPbiZVBudE");
        }
    }

    public void getAllPlayList(final ArrayList selectedPlayListsID){
        String url = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&key="+ currentAPIkey+"&channelId=UCAdN7ghumPNFA7Ww3vf_KpQ&maxResults=50";
        Log.d(TAG, "getAllPlayList: " + selectedPlayListsID.size());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            playList =new Gson().fromJson(response, PlayList.class);

                                for (int y = 0; y <selectedPlayListsID.size() ; y++) {
                            for (int x = 0; x <playList.getItems().length ; x++) {
                                int flag=0;
                                    if (playList.getItems()[x].getId().equals(selectedPlayListsID.get(y))){
                                        Log.d(TAG, "onResponse:added///*/*/  " +playList.getItems()[x].getId() );
                                        mPlayLists.add(playList.getItems()[x]);
                                        flag=1;
                                    }
                                    if (flag==1)
                                        break;
                                }
                            }
                            // set adapter here
//                            sortPlaylists(playList,selectedPlayListsID);
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

        MySingleton.getmInsance(ViewPlayListsActivity.this).addToRequestQueue(stringRequest);
    }
    public void getAllPlayList(){
        String url = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&key="+currentAPIkey+"&channelId=UCAdN7ghumPNFA7Ww3vf_KpQ&maxResults=50";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            playList =new Gson().fromJson(response, PlayList.class);

                            for (int x = 0; x <playList.getItems().length ; x++) {
                                        Log.d(TAG, "onResponse:added///*/*/  " +playList.getItems()[x].getId() );
                                        mPlayLists.add(playList.getItems()[x]);
                                    }

                            // set adapter here
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

        MySingleton.getmInsance(ViewPlayListsActivity.this).addToRequestQueue(stringRequest);
    }



    public void initRecyclerView(){
        selectedPlaylistsRecycler = findViewById(R.id.selectedPlaylistsRecycler);
        selectedPlaylistsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DisplayingPlaylistsAdapter(ViewPlayListsActivity.this,mPlayLists);
        selectedPlaylistsRecycler.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(ViewPlayListsActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_allPlaylists) {
            if (playlistType.equals("allPlaylists")){
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }else {
                intent = new Intent(ViewPlayListsActivity.this, ViewPlayListsActivity.class);
                intent.putExtra("type","allPlaylists");
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.nav_latestVideos) {
            intent = new Intent(ViewPlayListsActivity.this, LatestVideosActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_Daily) {
            intent = new Intent(ViewPlayListsActivity.this, DailyActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_Fav) {
            startActivity(new Intent(ViewPlayListsActivity.this, FavouriteActivity.class));
            finish();
        } else if (id == R.id.nav_About) {
            startActivity(new Intent(ViewPlayListsActivity.this, AboutUsActivity.class));
            finish();
        } else if (id == R.id.nav_Contact) {
            startActivity(new Intent(ViewPlayListsActivity.this, ContactUsActivity.class));
            finish();
        } else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_Login) {
            if (mAuth.getCurrentUser() == null){
                startActivity(new Intent(ViewPlayListsActivity.this, AuthActivity.class));
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
