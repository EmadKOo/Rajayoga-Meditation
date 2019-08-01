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
import com.google.gson.Gson;

import java.util.ArrayList;

import emad.youtube.Adapters.DisplayingPlaylistsAdapter;
import emad.youtube.Model.PlayLists.PlayList;
import emad.youtube.Model.PlayLists.PlayListItems;
import emad.youtube.R;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.ViewPlayListsActivity;
import emad.youtube.VolleyUtils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlaylistsFragment extends Fragment {

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
    Bundle bundle ;
    private static final String TAG = "AllPlaylistsFragment";
    public AllPlaylistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_playlists, container, false);

        currentAPIkey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();

      //  bundle = getActivity().getIntent().getExtras();
        playlistType = getArguments().getString("type", "default");
        Log.d(TAG, "onCreateView: " + playlistType);
        initRecyclerView(view);
        if (!playlistType.equals("allPlaylists")){
            setUpIDs(playlistType);
            getAllPlayList(playlistsIDList);
        }else {
            getAllPlayList();
        }

        return view;
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
            playlistsIDList.add("PLkYTNBmHiJIiSPAtCFoWjW1eXyrK8xZD5");
            playlistsIDList.add("PLkYTNBmHiJIjfjzK95dG2cRwIE8WQ5GAb");
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

        MySingleton.getmInsance(getActivity()).addToRequestQueue(stringRequest);
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

        MySingleton.getmInsance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void initRecyclerView(View view){
        selectedPlaylistsRecycler = view.findViewById(R.id.selectedPlaylistsRecycler);
        selectedPlaylistsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DisplayingPlaylistsAdapter(getActivity(),mPlayLists);
        selectedPlaylistsRecycler.setAdapter(adapter);
    }
}
