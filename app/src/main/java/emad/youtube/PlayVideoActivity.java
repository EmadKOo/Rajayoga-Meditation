package emad.youtube;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionSnippet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import de.hdodenhof.circleimageview.CircleImageView;
import emad.youtube.Adapters.CommentsAdapter;
import emad.youtube.Adapters.LatestRestVideosAdapter;
import emad.youtube.Adapters.RestVideosAdapter;
import emad.youtube.Iterfaces.ChangeVideo;
import emad.youtube.Iterfaces.IRequestPermission;
import emad.youtube.Model.Comment.Comment;
import emad.youtube.Model.Comment.CoreSnippet;
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.Model.VideoList.ItemsListData;
import emad.youtube.Tools.DialogHelper;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PlayVideoActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener
        , YouTubePlayer.PlaybackEventListener
        , YouTubePlayer.PlayerStateChangeListener
        , ChangeVideo
        , IRequestPermission {

    String uniqueID = null;
    // channel bar
    CircleImageView channelImage;
    TextView channelName;
    TextView channelSubscribers;
    TextView videoViews;
    TextView videoName;
    ImageView showMore;
    ImageView downloadVideo;
    ImageView favIcon;
    TextView videoDesc;
    TextView likeBtn;
    TextView dislikeBtn;
    TextView subscribeBtn;
    int numOfClicks = 0;

    TextView noComments;
    RecyclerView commentsOfVideo;
    ArrayList<CoreSnippet> commentsArrayList = new ArrayList<>();
    CommentsAdapter commentsAdapter;
    Comment comment = new Comment();

    RecyclerView restOfVideos;
    ArrayList<ItemsListData> videoPlaylist;
    RestVideosAdapter adapter;

    // latest items
    ArrayList<Latest> latestArrayList = new ArrayList<>();
    ;
    LatestRestVideosAdapter latestAdapter;
    Latest latestVideo = new Latest();

    YouTubePlayerView playerView;
    YouTubePlayer mYouTubePlayer;
    ItemsListData currentVideo;
    String currentVideoID = "";
    String videoType = "";

    DatabaseReference favRef;
    DatabaseReference ratingRef;
    DatabaseReference allFavRef;
    ArrayList<Favourite> allUserFavouriteVideos = new ArrayList();
    Favourite favourite = new Favourite();
    Favourite LikeDislike = new Favourite();
    String reaction = "true";

    private LinearLayout mainLayout;
    private ProgressBar mainProgressBar;
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    String storagePermission[];
    String telephonyPermission[];
    private static final int TELEPPHONY_REQUEST_CODE = 440;
    private static final int STORAGE_REQUEST_CODE = 400;


    // Subscribtion
    ProgressBar progressBar;
    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static String youtubeLink;
    String YouTubeAPIChannelId = "UCAdN7ghumPNFA7Ww3vf_KpQ";
    private static final String[] SCOPES = {"https://www.googleapis.com/auth/plus.me",
            "https://www.googleapis.com/auth/youtube.force-ssl",
            "https://www.googleapis.com/auth/youtubepartner",
            "https://www.googleapis.com/auth/youtube"};

    DialogHelper dialogHelper;
    private static final String TAG = "PlayVideoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        dialogHelper = new DialogHelper(this, this);


        currentAPIKey = randomAPI.getRandomKey();
        initCredintials();

        initViews();
        channelData();
        subscribersNumber();
        videoType = getIntent().getStringExtra("type");
        if (videoType.equals("latest")) {
            // work with rest latest videos adapter
            latestVideo = (Latest) getIntent().getSerializableExtra("currentVideo");
            latestArrayList = (ArrayList<Latest>) getIntent().getSerializableExtra("allList");
            Log.d(TAG, "onCreate: latestArrayList " + latestArrayList.size());
            currentVideoID = latestVideo.getVideoId();
            videoName.setText(latestVideo.getTitle());
            latestArrayList = filterLatestRestPlaylist(latestArrayList, currentVideoID);
            initRecyclerView("latest");
            initCommentsRecycler();
            videoViews(currentVideoID);

            channelName.setText(latestVideo.getChannelTitle());
            videoDesc.setText(latestVideo.getDescription());

            favourite.setChannelTitle(latestVideo.getChannelTitle());
            favourite.setTitle(latestVideo.getTitle());
            favourite.setUrl(latestVideo.getUrl());
//            favourite.setDescription(latestVideo.getDescription());
            favourite.setPublishedAt(latestVideo.getPublishedAt());
            favourite.setVideoID(latestVideo.getVideoId());
            favourite.setReaction("true");
            getAllLikes(currentVideoID);
            setFavouriteAction(currentVideoID);
            getLatestDescription(currentVideoID);
            Log.d(TAG, "onCreate: DES " + latestVideo.getDescription());
        } else {
            currentVideo = (ItemsListData) getIntent().getSerializableExtra("currentVideo");
            videoPlaylist = (ArrayList<ItemsListData>) getIntent().getSerializableExtra("allList");
            currentVideoID = currentVideo.getSnippet().getResourceId().getVideoId();
            videoPlaylist = filterRestPlaylist(videoPlaylist, currentVideoID);
            videoName.setText(currentVideo.getSnippet().getTitle());

            initRecyclerView("original");
            initCommentsRecycler();
            videoViews(currentVideoID);
            channelName.setText(currentVideo.getSnippet().getChannelTitle());
            videoDesc.setText(currentVideo.getSnippet().getDescription());

            favourite.setChannelTitle(currentVideo.getSnippet().getChannelTitle());
            favourite.setTitle(currentVideo.getSnippet().getTitle());
            favourite.setUrl(currentVideo.getSnippet().getThumbnails().getMedium().getUrl());
            favourite.setDescription(currentVideo.getSnippet().getDescription());
            favourite.setPublishedAt(currentVideo.getSnippet().getPublishedAt());
            favourite.setVideoID(currentVideo.getSnippet().getResourceId().getVideoId());
            favourite.setReaction("true");

            setFavouriteAction(currentVideoID);
            getAllLikes(currentVideoID);
        }

        playerView.initialize(currentAPIKey, PlayVideoActivity.this);
        getLikedDislikedBefore();
        loadComments(currentVideoID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        telephonyPermission = new String[]{Manifest.permission.READ_PHONE_STATE};

        if (checkTelephonyPermission())
            getID();
//        else
//            requestTelephonyPermission();
    }

    public void initRecyclerView(String type) {
        restOfVideos = findViewById(R.id.restOfVideos);
        restOfVideos.setLayoutManager(new LinearLayoutManager(this));
        if (type.equals("latest")) {
            latestAdapter = new LatestRestVideosAdapter(PlayVideoActivity.this, latestArrayList, this);
            restOfVideos.setAdapter(latestAdapter);
        } else {
            adapter = new RestVideosAdapter(PlayVideoActivity.this, videoPlaylist, this);
            restOfVideos.setAdapter(adapter);
        }
    }

    public void initViews() {
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainProgressBar = (ProgressBar) findViewById(R.id.prgrBar);
        playerView = findViewById(R.id.playerView);
        channelImage = findViewById(R.id.channelImage);
        videoName = findViewById(R.id.videoName);
        favIcon = findViewById(R.id.favIcon);
        videoViews = findViewById(R.id.videoViews);
        channelName = findViewById(R.id.channelName);
        channelSubscribers = findViewById(R.id.channelSubscribers);
        showMore = findViewById(R.id.showMore);
        downloadVideo = findViewById(R.id.downloadVideo);
        videoDesc = findViewById(R.id.videoDesc);
        noComments = findViewById(R.id.noComments);
        likeBtn = findViewById(R.id.likeBtn);
        dislikeBtn = findViewById(R.id.dislikeBtn);
        subscribeBtn = findViewById(R.id.subscribeBtn);
        progressBar = findViewById(R.id.progress_bar);
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfClicks % 2 == 0) {
                    videoDesc.setVisibility(View.VISIBLE);
                    showMore.setImageResource(R.drawable.less_black);
                } else {
                    videoDesc.setVisibility(View.GONE);
                    showMore.setImageResource(R.drawable.more_black);
                }
                numOfClicks++;
            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSharedPreferences().equals("default")) {
                    subscribe();

                } else if (getSharedPreferences().equals("Subscribed")) {
                    subscribeBtn.setText("Subscribe");
                    subscribeBtn.setTextColor(getResources().getColor(R.color.black));
                    addToSharedPreferences("Subscribe");
                } else if (getSharedPreferences().equals("Subscribe")) {
                    subscribeBtn.setText("Subscribed");
                    subscribeBtn.setTextColor(getResources().getColor(R.color.youtubelogo));
                    addToSharedPreferences("Subscribed");
                }
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: RESULT " + result);
                if (result.equals("1")) {
                    addLikeDislike("0");
                } else if (result.equals("0") || result.equals("-1")) {
                    addLikeDislike("1");
                }
            }
        });

        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.equals("-1")) {
                    addLikeDislike("0");
                } else if (result.equals("0") || result.equals("1")) {
                    addLikeDislike("-1");
                }
            }
        });
        if (getSharedPreferences().equals("default")) {
            Log.d(TAG, "initViews: HELLLLLLLLO  default");
            subscribeBtn.setText("Subscribe");
            subscribeBtn.setTextColor(getResources().getColor(R.color.black));
        } else if (getSharedPreferences().equals("Subscribed")) {
            Log.d(TAG, "initViews: HELLLLLLLLO  Subscribed");
            subscribeBtn.setText("Subscribed");
            subscribeBtn.setTextColor(getResources().getColor(R.color.youtubelogo));
        } else if (getSharedPreferences().equals("Subscribe")) {
            Log.d(TAG, "initViews: HELLLLLLLLO  Subscribe");
            subscribeBtn.setText("Subscribe");
            subscribeBtn.setTextColor(getResources().getColor(R.color.black));
        }

        downloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    String ytLink = "https://www.youtube.com/watch?v=" + currentVideoID + "&t=1s";
                    Log.d(TAG, "onClickfd:     " + ytLink);
                    youtubeLink = ytLink;
                    mainLayout.setVisibility(View.VISIBLE);
                    getYoutubeDownloadUrl(youtubeLink);
                   // getMYoutubeDownloadUrl(youtubeLink);
                } else {
                    Log.d(TAG, "onClickfd: REQUEST");
                    //requestStoragePermission();
                    dialogHelper.displayDialog("1");
                }
            }
        });
    }

    public void initCommentsRecycler() {
        commentsOfVideo = findViewById(R.id.commentsOfVideo);
        commentsOfVideo.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(commentsArrayList, PlayVideoActivity.this);
        commentsOfVideo.setAdapter(commentsAdapter);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (youTubePlayer != null) {
            try {
                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.setPlayerStateChangeListener(this);
                mYouTubePlayer.setPlaybackEventListener(this);
                if (!b)
                    mYouTubePlayer.loadVideo(currentVideoID);
                //youTubePlayer.cueVideo(currentVideoID);
            } catch (Exception ex) {
                Log.d(TAG, "onInitializationSuccess: " + ex.getMessage());
            }
        }
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

    @Override
    public void videoChanged(ItemsListData itemsListData) {
        Log.d(TAG, "videoChanged: " + itemsListData.getSnippet().getResourceId().getVideoId());
        if (mYouTubePlayer != null) {
            mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            mYouTubePlayer.loadVideo(itemsListData.getSnippet().getResourceId().getVideoId());
            mYouTubePlayer.play();

            currentVideoID = itemsListData.getSnippet().getResourceId().getVideoId();
            videoPlaylist = filterRestPlaylist((ArrayList<ItemsListData>) getIntent().getSerializableExtra("allList"), itemsListData.getSnippet().getResourceId().getVideoId());
            adapter = new RestVideosAdapter(PlayVideoActivity.this, videoPlaylist, this);
            restOfVideos.setAdapter(adapter);

            videoName.setText(itemsListData.getSnippet().getTitle());
            videoDesc.setText(itemsListData.getSnippet().getDescription());
            videoViews(currentVideoID);
            loadComments(currentVideoID);


            favourite.setTitle(itemsListData.getSnippet().getTitle());
            favourite.setUrl(itemsListData.getSnippet().getThumbnails().getHigh().getUrl());
            favourite.setDescription(itemsListData.getSnippet().getDescription());
            favourite.setPublishedAt(itemsListData.getSnippet().getPublishedAt());
            favourite.setVideoID(itemsListData.getSnippet().getResourceId().getVideoId());
            favourite.setReaction("true");
            reaction = "";

            getAllLikes(currentVideoID);
            setFavouriteAction(currentVideoID);


        }
    }

    @Override
    public void latestVideoChanged(Latest latestVideo) {
        Log.d(TAG, "videoChanged: " + latestArrayList.get(0).getVideoId());
        if (mYouTubePlayer != null) {
            mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            mYouTubePlayer.loadVideo(latestVideo.getVideoId());
            mYouTubePlayer.play();

            currentVideoID = latestVideo.getVideoId();
            latestArrayList = filterLatestRestPlaylist((ArrayList<Latest>) getIntent().getSerializableExtra("allList"), currentVideoID);
            latestAdapter = new LatestRestVideosAdapter(PlayVideoActivity.this, latestArrayList, this);
            restOfVideos.setAdapter(latestAdapter);

            videoName.setText(latestVideo.getTitle());
            videoDesc.setText(latestVideo.getDescription());
            videoViews(currentVideoID);
            loadComments(currentVideoID);


            favourite.setTitle(latestVideo.getTitle());
            favourite.setUrl(latestVideo.getUrl());
            favourite.setDescription(latestVideo.getDescription());
            favourite.setPublishedAt(latestVideo.getPublishedAt());
            favourite.setVideoID(latestVideo.getVideoId());
            favourite.setReaction("true");
            reaction = "";

            getAllLikes(currentVideoID);
            setFavouriteAction(currentVideoID);


        }
    }

    @Override
    public void favouriteVideoChanged(Favourite favourite) {

    }

    public ArrayList<ItemsListData> filterRestPlaylist(ArrayList<ItemsListData> rest, String currentID) {
        ArrayList<ItemsListData> filteredList = new ArrayList<>();
        for (int x = 0; x < rest.size(); x++) {
            if (!rest.get(x).getSnippet().getResourceId().getVideoId().equals(currentID))
                filteredList.add(rest.get(x));
        }

        return filteredList;
    }

    public ArrayList<Latest> filterLatestRestPlaylist(ArrayList<Latest> rest, String currentID) {
        ArrayList<Latest> filteredList = new ArrayList<>();
        for (int x = 0; x < rest.size(); x++) {
            if (!rest.get(x).getVideoId().equals(currentID))
                filteredList.add(rest.get(x));
        }
        return filteredList;
    }

    public void loadComments(String videoID) {
        String url = "https://www.googleapis.com/youtube/v3/commentThreads?key=" + currentAPIKey + "&textFormat=plainText&part=snippet&videoId=" + videoID + "&maxResults=50";
        comment = new Comment();
        commentsArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            comment = new Gson().fromJson(response, Comment.class);
                            if (comment.getPageInfo().getTotalResults() == 0) {
                                commentsOfVideo.setVisibility(View.GONE);
                                noComments.setVisibility(View.VISIBLE);
                            }
                            for (int x = 0; x < comment.getItems().length; x++) {
                                commentsArrayList.add(comment.getItems()[x].getSnippet().getTopLevelComment().getSnippet());
                            }
                            commentsAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: " + error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(PlayVideoActivity.this).addToRequestQueue(stringRequest);

    }

    public void channelData() {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet&fields=items%2Fsnippet%2Fthumbnails%2Fhigh&id=UCAdN7ghumPNFA7Ww3vf_KpQ&key=" + currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");

                            String imgUrl = jsonData.getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                            try {
                                Picasso.get().load(imgUrl).placeholder(R.drawable.placeholder).into(channelImage);
                            } catch (Exception ex) {
                                Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(channelImage);
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: " + error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(PlayVideoActivity.this).addToRequestQueue(stringRequest);

    }

    public void subscribersNumber() {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=UCAdN7ghumPNFA7Ww3vf_KpQ&key=" + currentAPIKey;
        comment = new Comment();
        commentsArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");

                            channelSubscribers.setText(jsonData.getJSONObject(0).getJSONObject("statistics").getString("subscriberCount") + " subscribers");
                        } catch (Exception ex) {
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: " + error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(PlayVideoActivity.this).addToRequestQueue(stringRequest);

    }

    public void videoViews(String mVideoID) {
        String url = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=" + mVideoID + "&key=" + currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");

                            videoViews.setText(jsonData.getJSONObject(0).getJSONObject("statistics").getString("viewCount") + " views");

                        } catch (Exception ex) {
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: " + error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(PlayVideoActivity.this).addToRequestQueue(stringRequest);

    }

    public void getAllLikes(final String videoID) {
        // get all likes

        if (uniqueID != null) {
// الحل اوصل للنود بتاعت الفيديو علطول احسن
            allUserFavouriteVideos.clear();
            allFavRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID).child(videoID);
            allFavRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange:getAllLikes)) " + dataSnapshot);
                    if (dataSnapshot.getValue() == null) {
                        favIcon.setImageResource(R.drawable.dislike);
                    } else {
                        if (dataSnapshot.child("reaction").getValue().equals("true"))
                            favIcon.setImageResource(R.drawable.fav_ico);
                        else
                            favIcon.setImageResource(R.drawable.dislike);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void setFavouriteAction(final String videoID) {
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addFavs(videoID);
            }
        });
    }

    private void addFavs(final String videoID){
        if (uniqueID != null) {
            Log.d(TAG, "onClick: uniqueID null");
            favRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID).child(videoID);
            favRef.setValue(favourite);
            Log.d(TAG, "onClick: getValue ");
            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID).child(videoID);
                    if (dataSnapshot.getValue() == null) {
                        // push favourite obj
                        reaction = "true";
//                            favRef.setValue(favourite);
                    } else {
                        if (favourite.getReaction().equals("true")) {
//                                favRef.child("reaction").setValue("false");
                            reaction = "false";
                        } else if (favourite.getReaction().equals("false")) {
//                                favRef.child("reaction").setValue("true");
                            reaction = "true";
                        }
                        Log.d(TAG, "onDataChange:else value " + dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            favRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID).child(videoID);
            if (reaction.equals("true")) {
                favRef.child("reaction").setValue("true");
                favourite.setReaction("true");
                favIcon.setImageResource(R.drawable.fav_ico);
            } else if (reaction.equals("false")) {
//                    favRef.child("reaction").setValue("false");
                favRef.removeValue();
                favRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID).child(videoID);
                favourite.setReaction("false");
                favIcon.setImageResource(R.drawable.dislike);
            } else if (reaction.equals("null")) {
                favRef.child("reaction").setValue("true");
                favourite.setReaction("true");
                favIcon.setImageResource(R.drawable.fav_ico);
            }

        }else {
            Log.d(TAG, "addFavs: " + uniqueID);
            dialogHelper.displayDialog("0");
        }
    }
    public void initCredintials() {
        mCredential = GoogleAccountCredential.usingOAuth2(
                PlayVideoActivity.this.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    private void subscribe() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d(TAG, "No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog((Activity) getApplicationContext(), connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), android.Manifest.permission.GET_ACCOUNTS)) {
            startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        } else {
            EasyPermissions.requestPermissions(this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.e(TAG, "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                } else {
                    subscribe();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        subscribe();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    subscribe();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    String ytLink = "https://www.youtube.com/watch?v=" + currentVideoID + "&t=1s";
                    Log.d(TAG, "onRequestPermissionsResult: " + ytLink);
                    youtubeLink = ytLink;
                    mainLayout.setVisibility(View.VISIBLE);
                    getYoutubeDownloadUrl(youtubeLink);
                  //  getMYoutubeDownloadUrl(youtubeLink);
                } else {
                  //  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == TELEPPHONY_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    // add to favourites
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    getID();
                    addFavs(currentVideoID);
                } else {
                  //  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void requestTelephone() {
        requestTelephonyPermission();
    }

    @Override
    public void requestStorage() {
        requestStoragePermission();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, Subscription> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Rajayoga Meditation")
//                    .setApplicationName("Commodity Alert")
                    .build();
        }

        @Override
        protected Subscription doInBackground(Void... params) {
            try {
                Subscription subscription = getDataFromApi();
                return subscription;
            } catch (Exception e) {
                mLastError = e;
                Log.e(TAG, "Error: " + e.toString());
                cancel(true);
                return null;
            }
        }

        private Subscription getDataFromApi() throws IOException {
            ResourceId resourceId = new ResourceId();
            resourceId.setChannelId(YouTubeAPIChannelId);
            resourceId.setKind("youtube#channel");

            SubscriptionSnippet snippet = new SubscriptionSnippet();
            snippet.setResourceId(resourceId);

            Subscription subscription = new Subscription();
            subscription.setSnippet(snippet);

            YouTube.Subscriptions.Insert subscriptionInsert = mService.subscriptions().insert("snippet,contentDetails", subscription);

            Subscription returnedSubscription = subscriptionInsert.execute();

            return returnedSubscription;
        }


        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected void onPostExecute(Subscription output) {
            hideProgress();
            if (output == null || output.size() == 0) {
                Log.e(TAG, "Not Subscribed");
            } else {
                subscribeBtn.setText("Subscribed");
                subscribeBtn.setTextColor(getResources().getColor(R.color.youtubelogo));
                addToSharedPreferences("Subscribed");
//                subscribeBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        @Override
        protected void onCancelled() {
            hideProgress();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else if (mLastError instanceof GoogleJsonResponseException) {
//                    Toast.makeText(getApplicationContext(), "GoogleJsonResponseException code: "
//                            + ((GoogleJsonResponseException) mLastError).getDetails().getCode() + " : "
//                            + ((GoogleJsonResponseException) mLastError).getDetails().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "The following error occurred:\n" + mLastError.getMessage());
                }
            } else {
                Log.e(TAG, "Request cancelled.");
            }
        }

    }

    protected void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public void addToSharedPreferences(String locationID) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("subscribe", locationID);
        editor.commit();
    }

    public String getSharedPreferences() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String locationID = sharedPref.getString("subscribe", "default");
        return locationID;
    }


    public void addLikeDislike(String rate) { // rate 1 like ,, rate 1 dislike ,, rate 0 no like no dislike

        if (uniqueID != null) {

            ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(uniqueID).child(currentVideoID);
            LikeDislike = favourite;
            LikeDislike.setReaction(rate);
            ratingRef.setValue(LikeDislike);
        }
    }


    String result = "";

    public String getLikedDislikedBefore() { // 1 if liked, -1 for dislike , 0 for not found
        if (uniqueID != null) {

            ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(uniqueID).child(currentVideoID);
            ratingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Log.d(TAG, "getLikedDislikedBefore: " + dataSnapshot);

                        Log.d(TAG, "onDataChange:getLikedDislikedBefore " + dataSnapshot.getValue());
                        Log.d(TAG, "onDataChange:getLikedDislikedBeforeCHild " + LikeDislike.getReaction());
                        Log.d(TAG, "getLikedDislikedBefore:reaction " + dataSnapshot.child("reaction"));
                        Log.d(TAG, "getLikedDislikedBefore:reaction " + dataSnapshot.child("reaction").getValue());
                        if (dataSnapshot.child("reaction").getValue() == null) {
                            dislikeBtn.setTextColor(getResources().getColor(R.color.black));
                            likeBtn.setTextColor(getResources().getColor(R.color.black));
                            Log.d(TAG, "onDataChange: INSIDE NNNulll");

                            likeBtn.setText("Like");
                            dislikeBtn.setText("Dislike");
                            result = "0";
                        } else if (dataSnapshot.child("reaction").getValue().toString().equals("1")) {
                            Log.d(TAG, "onDataChange: INSIDE");
                            likeBtn.setTextColor(getResources().getColor(R.color.youtubelogo));
                            dislikeBtn.setTextColor(getResources().getColor(R.color.black));
                            likeBtn.setText("Liked");
                            dislikeBtn.setText("Dislike");
                            result = "1";
                        } else if (dataSnapshot.child("reaction").getValue().toString().equals("-1")) {
                            dislikeBtn.setTextColor(getResources().getColor(R.color.youtubelogo));
                            likeBtn.setTextColor(getResources().getColor(R.color.black));
                            Log.d(TAG, "onDataChange: INSIDE");

                            likeBtn.setText("Like");
                            dislikeBtn.setText("Disliked");
                            result = "-1";
                        } else if (dataSnapshot.child("reaction").getValue().toString().equals("0")) {
                            dislikeBtn.setTextColor(getResources().getColor(R.color.black));
                            likeBtn.setTextColor(getResources().getColor(R.color.black));
                            Log.d(TAG, "onDataChange: INSIDE");

                            likeBtn.setText("Like");
                            dislikeBtn.setText("Dislike");
                            result = "0";
                        }
                    } else {
                        // no reaction
                        result = "0";
                        Log.d(TAG, "getLikedDislikedBefore: 0 ");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return result;
    }

    private void getYoutubeDownloadUrl(String youtubeLink) {
        new YouTubeExtractor(this) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                mainProgressBar.setVisibility(View.GONE);
                mainLayout.removeAllViews();
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    finish();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        Log.d(TAG, "onExtractionComplete: VMETA " + vMeta.getTitle());
                        addButtonToMainLayout(vMeta.getTitle(), ytFile);
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }

    private void addButtonToMainLayout(final String videoTitle, final YtFile ytfile) {
        // Display some buttons and let the user choose the format
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Button btn = new Button(this);
        btn.setText(btnText);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mainLayout.setVisibility(View.GONE);
        if (videoTitle!= null){
                    String filename;
                    Log.d(TAG, "onClick: TITLE " + videoTitle);

                    if (videoTitle.length() > 55) {
                        filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
                    } else {
                        filename = videoTitle + "." + ytfile.getFormat().getExt();
                    }
                    filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
                    downloadFromUrl(ytfile.getUrl(), videoTitle, filename);

        }
                }
            });
            mainLayout.addView(btn);
    }

    private void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/Rajayoga Meditation", fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    @Override
    public void onBackPressed() {
        if (mainLayout.getVisibility() == View.VISIBLE)
            mainLayout.setVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    public void getLatestDescription(String mVideoID) {
        String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + mVideoID + "&key=" + currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");
                            String desc = jsonData.getJSONObject(0).getJSONObject("snippet").getString("description");
                            Log.d(TAG, "onResponse: DESC " + desc);
                            videoDesc.setText(desc);
                            favourite.setDescription(desc);

                        } catch (Exception ex) {
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: " + error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(PlayVideoActivity.this).addToRequestQueue(stringRequest);

    }

    public void requestTelephonyPermission() {
        ActivityCompat.requestPermissions(this, telephonyPermission, TELEPPHONY_REQUEST_CODE);
    }
    private boolean checkTelephonyPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }
    private void getID() {
        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        uniqueID = mTelephonyMgr.getSubscriberId();
        Log.d(TAG, "getID: imsi " + uniqueID);
        Log.d(TAG, "getID: imsi 2  "  + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    private void getMYoutubeDownloadUrl(String youtubeLink) {

        Log.d(TAG, "getMYoutubeDownloadUrl: " + youtubeLink);
        new YouTubeExtractor(this) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                mainProgressBar.setVisibility(View.GONE);

                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    finish();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        addMButtonToMainLayout(vMeta.getTitle(), ytFile);
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }

    private void addMButtonToMainLayout(final String videoTitle, final YtFile ytfile) {
        // Display some buttons and let the user choose the format
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Button btn = new Button(this);
        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String filename;
                if (videoTitle.length() > 55) {
                    filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
                } else {
                    filename = videoTitle + "." + ytfile.getFormat().getExt();
                }
                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
                downloadMFromUrl(ytfile.getUrl(), videoTitle, filename);

            }
        });
        mainLayout.addView(btn);
    }

    private void downloadMFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/Rajayoga Meditation", fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}