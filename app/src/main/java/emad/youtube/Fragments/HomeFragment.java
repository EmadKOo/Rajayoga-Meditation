package emad.youtube.Fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import emad.youtube.AboutUsActivity;
import emad.youtube.HomeActivity;
import emad.youtube.Model.Video.Video;
import emad.youtube.NotificationsActivity;
import emad.youtube.R;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class HomeFragment extends Fragment implements
    //    YouTubePlayer.OnInitializedListener
        YouTubePlayer.PlaybackEventListener
        ,YouTubePlayer.PlayerStateChangeListener {


    YouTubePlayer mYouTubePlayer;
    YouTubePlayerSupportFragment frag;
    String videoID;
    Video video;
    RelativeLayout dailyMurli;
    RelativeLayout murliRevision;
    RelativeLayout experienceMeditaion;
    RelativeLayout dailyInspiration;
    TextView txdailyMurli;
    TextView txmurliRevision;
    TextView txexperienceMeditaion;
    TextView txdailyInspiration;
    ImageView goToSearch;

    YouTubePlayerSupportFragment youTubePlayerFragment;
    Intent intent;
    FirebaseAuth mAuth;
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    Fragment fragment ;

    Bundle bundle;
    private static final String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        currentAPIKey = randomAPI.getRandomKey();
        Log.d(TAG, "onCreate: APIIIIIII  " + currentAPIKey);
        mAuth = FirebaseAuth.getInstance();
        initViews(view);
       // frag = (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, youTubePlayerFragment).commit();
        getLatestVideoID();
        return view;
    }


//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        mYouTubePlayer = youTubePlayer;
//        mYouTubePlayer.setPlayerStateChangeListener(this);
//        mYouTubePlayer.setPlaybackEventListener(this);
//        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//        if (!b)
//            mYouTubePlayer.cueVideo(videoID);
//
//        Log.d(TAG, "onInitializationSuccess: ");
//
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//    }

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

    public void initViews(View view){
      //  goToSearch = view.findViewById(R.id.goToSearch);
        dailyMurli = view.findViewById(R.id.dailyMurli);
        murliRevision= view.findViewById(R.id.murliRevision);
        experienceMeditaion= view.findViewById(R.id.experienceMeditaion);
        dailyInspiration= view.findViewById(R.id.dailyInspiration);

        txdailyMurli = view.findViewById(R.id.txdailyMurli);
        txmurliRevision = view.findViewById(R.id.txmurliRevision);
        txexperienceMeditaion = view.findViewById(R.id.txexperienceMeditaion);
        txdailyInspiration = view.findViewById(R.id.txdailyInspiration);

        txdailyMurli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBundle("type","dailyMurli", new ColorDrawable(0xff009fce), "Shiv Baba English Murli");
            }
        });

        txmurliRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBundle("type","murliRevision" , new ColorDrawable(0xff009fce),"Avyakt Murli Revision");
            }
        });

        txexperienceMeditaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBundle("type","experienceMeditaion" , new ColorDrawable(0xff009fce), "Experience Meditation");
            }
        });
        txdailyInspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DailyFragment();

                try {
                    ((HomeActivity) getActivity()).loadFragment(fragment);
                    ((HomeActivity) getActivity()).setCurrentFragment(fragment,  new ColorDrawable(0xff009fce), "Daily Inspirations");
                }catch (Exception e){
                    ((NotificationsActivity) getActivity()).loadFragment(fragment);
                    ((NotificationsActivity) getActivity()).setCurrentFragment(fragment,  new ColorDrawable(0xff009fce), "Daily Inspirations");

                }
            }
        });
        dailyMurli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    dailyMurli.setBackground(getActivity().getDrawable(R.drawable.daily_murli_clicked));
//                }
//                getContext().startActivity(new Intent(getActivity(), AboutUsActivity.class));

                initBundle("type","dailyMurli", new ColorDrawable(0xff009fce), "Shiv Baba English Murli" );
            }
        });

        murliRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBundle("type","murliRevision" , new ColorDrawable(0xff009fce), "Avyakt Murli Revision");
            }
        });

        experienceMeditaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBundle("type","experienceMeditaion", new ColorDrawable(0xff009fce), "Experience Meditation" );
            }
        });
        dailyInspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DailyFragment();

                try {
                    ((HomeActivity) getActivity()).loadFragment(fragment);
                    ((HomeActivity) getActivity()).setCurrentFragment(fragment, new ColorDrawable(0xff009fce), "Daily Inspirations");
                }catch (Exception e){
                    ((NotificationsActivity) getActivity()).loadFragment(fragment);
                    ((NotificationsActivity) getActivity()).setCurrentFragment(fragment, new ColorDrawable(0xff009fce), "Daily Inspirations");

                }
            }
        });

//        goToSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initBundle(String key, String value, ColorDrawable colorDrawable, String title){
        bundle = new Bundle();
        bundle.putString(key, value);

        fragment = new AllPlaylistsFragment();
        fragment.setArguments(bundle);

        try {
            ((HomeActivity) getActivity()).loadFragment(fragment);
            ((HomeActivity) getActivity()).setCurrentFragment(fragment, colorDrawable, title);
        }catch (Exception e){
            ((NotificationsActivity) getActivity()).loadFragment(fragment);
            ((NotificationsActivity) getActivity()).setCurrentFragment(fragment, colorDrawable, title);

        }

    }
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

                            youTubePlayerFragment.initialize(currentAPIKey, new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    mYouTubePlayer = youTubePlayer;
                                    mYouTubePlayer.setPlayerStateChangeListener(HomeFragment.this);
                                    mYouTubePlayer.setPlaybackEventListener(HomeFragment.this);
                                    mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                    if (!b)
                                        mYouTubePlayer.cueVideo(videoID);

                                    Log.d(TAG, "onInitializationSuccess: ");
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });

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

        MySingleton.getmInsance(getActivity()).addToRequestQueue(stringRequest);
    }

}
