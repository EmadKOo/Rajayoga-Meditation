package emad.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import emad.youtube.Adapters.DailyAdapter;
import emad.youtube.Adapters.SearchAdapter;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

public class SearchActivity extends AppCompatActivity {

    RecyclerView searchRecyclerView;
    EditText searchEditText;
    ImageView searchBack;

    SearchAdapter adapter;
    ArrayList<Latest> searchList = new ArrayList<>();
    Latest latest = new Latest();
    String url="";
    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    private static final String TAG = "SearchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        currentAPIKey = randomAPI.getRandomKey();
        initRecyclerView();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getAllVideos(searchEditText.getText().toString());
            }

        });

    }

    public void initRecyclerView(){
        searchBack = findViewById(R.id.searchBack);
        searchEditText = findViewById(R.id.searchEditText);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(searchList,SearchActivity.this);
        searchRecyclerView.setAdapter(adapter);

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getAllVideos(String searchWord){
        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCAdN7ghumPNFA7Ww3vf_KpQ&q="+searchWord+"&key="+currentAPIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "onResponse: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");
                            searchList.clear();
                            for (int x = 0; x <jsonData.length() ; x++) {

                                latest = new Latest();
                                if (jsonData.getJSONObject(x).getJSONObject("id").has("videoId")){
                                    latest.setVideoId(jsonData.getJSONObject(x).getJSONObject("id").getString("videoId"));
                                    latest.setPublishedAt(jsonData.getJSONObject(x).getJSONObject("snippet").getString("publishedAt"));
                                    latest.setTitle(jsonData.getJSONObject(x).getJSONObject("snippet").getString("title"));
                                    latest.setDescription(jsonData.getJSONObject(x).getJSONObject("snippet").getString("description"));
                                    latest.setUrl(jsonData.getJSONObject(x).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
                                    latest.setChannelTitle(jsonData.getJSONObject(x).getJSONObject("snippet").getString("channelTitle"));

                                    searchList.add(latest);
                                }

                            }

                            Log.d(TAG, "onResponse: SIZE " + searchList.size());
                            adapter = new SearchAdapter(searchList,SearchActivity.this);
                            searchRecyclerView.setAdapter(adapter);

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

        MySingleton.getmInsance(SearchActivity.this).addToRequestQueue(stringRequest);
    }
}
