package emad.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import emad.youtube.Adapters.DisplayVideosPlaylistAdapter;
import emad.youtube.Adapters.DisplayingPlaylistsAdapter;
import emad.youtube.Model.PlayLists.PlayListItems;
import emad.youtube.Model.Video.Video;
import emad.youtube.Model.VideoList.ItemsListData;
import emad.youtube.Model.VideoList.VideoList;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class ViewPlaylistVideosActivity extends AppCompatActivity {

    TextView playlistName;
    ImageView imgBackIco;

    RecyclerView videosOfSelectedPlaylistsRecycler;
    DisplayVideosPlaylistAdapter adapter;
    ArrayList<ItemsListData> videoArrayList = new ArrayList<>();

    PlayListItems selectedPlaylist = new PlayListItems();
    VideoList videoList = new VideoList();
    Video latestVideos = new Video();
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    private static final String TAG = "ViewPlaylistVideosActiv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_playlist_videos);

        currentAPIKey = randomAPI.getRandomKey();
        Toolbar toolbar = findViewById(R.id.toolbar);
        playlistName = findViewById(R.id.playlistName);
        imgBackIco = findViewById(R.id.imgBackIco);
        imgBackIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initRecyclerView();
        selectedPlaylist = (PlayListItems)getIntent().getSerializableExtra("playlist");
        playlistName.setText(selectedPlaylist.getSnippet().getTitle());
        Log.d(TAG, "onCreate: " + selectedPlaylist.getSnippet().getTitle());
        getVideosOfSelectedPlaylist();



    }

    public void getVideosOfSelectedPlaylist(){
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+selectedPlaylist.getId()+"&key="+ currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            videoList =new Gson().fromJson(response, VideoList.class);

                            for (int x = 0; x <videoList.getItems().length ; x++) {
                                if (!videoList.getItems()[x].getSnippet().getTitle().equals("Private video")){
                                    if (!videoList.getItems()[x].getSnippet().getTitle().equals("Deleted video")){
                                        videoArrayList.add(videoList.getItems()[x]);
                                    }
                                }
                            }
                            Log.d(TAG, "onResponse: LENGTH   " + videoArrayList.size());

                        // notify adapter here
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

        MySingleton.getmInsance(ViewPlaylistVideosActivity.this).addToRequestQueue(stringRequest);

    }

    public void initRecyclerView(){
        videosOfSelectedPlaylistsRecycler = findViewById(R.id.videosOfSelectedPlaylistsRecycler);
     //   videosOfSelectedPlaylistsRecycler.setLayoutManager(new LinearLayoutManager(this));
        videosOfSelectedPlaylistsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DisplayVideosPlaylistAdapter(ViewPlaylistVideosActivity.this,videoArrayList);
        videosOfSelectedPlaylistsRecycler.setAdapter(adapter);
    }
}
