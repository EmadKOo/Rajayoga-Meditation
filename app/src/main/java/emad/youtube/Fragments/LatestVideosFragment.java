package emad.youtube.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import emad.youtube.Adapters.LatestVideosAdapter;
import emad.youtube.LatestVideosActivity;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.R;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestVideosFragment extends Fragment {

    RecyclerView latestVideosRecycler;
    LatestVideosAdapter adapter;
    Latest latest = new Latest();
    ArrayList<Latest> latestVideos = new ArrayList<>();
    Intent intent;
    FirebaseAuth mAuth;

    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";

    private static final String TAG = "LatestVideosFragment";

    public LatestVideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_videos, container, false);

        currentAPIKey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();
        initRecyclerView(view);
        getLatest();

        return view;
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

        MySingleton.getmInsance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void initRecyclerView(View view){
        latestVideosRecycler = view.findViewById(R.id.latestVideosRecycler);
        latestVideosRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LatestVideosAdapter(latestVideos,getActivity());
        latestVideosRecycler.setAdapter(adapter);
    }


}
