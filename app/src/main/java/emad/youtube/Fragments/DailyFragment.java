package emad.youtube.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import emad.youtube.Adapters.DailyAdapter;
import emad.youtube.DailyActivity;
import emad.youtube.R;
import emad.youtube.VolleyUtils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends Fragment {

    ArrayList allPhotos = new ArrayList();
    RecyclerView instagramRecycler;
    DailyAdapter adapter;
    Intent intent;
    FirebaseAuth mAuth;
    DatabaseReference instaRef;
    RelativeLayout followInsta;
    TextView instaTitle;
    TextView instaDesc;

    private static final String TAG = "DailyFragment";

    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        mAuth = FirebaseAuth.getInstance();
        initViews(view);
        initRecyclerView(view);


        getDaily();

        return view;
    }

    public void initViews(View view){
        followInsta = view.findViewById(R.id.followInsta);
        instaTitle = view.findViewById(R.id.instaTitle);
        instaDesc = view.findViewById(R.id.instaDesc);

        instaRef = FirebaseDatabase.getInstance().getReference().child("instaqoute");
        instaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                instaTitle.setText(dataSnapshot.child("title").getValue().toString());
                instaDesc.setText(dataSnapshot.child("desc").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        followInsta.setOnClickListener(new View.OnClickListener() {
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
    }
    public void getDaily(){
        String url = "https://api.instagram.com/v1/users/self/media/recent?access_token=10169293354.1677ed0.b1fc3fca0c9849fb9d9627cc6f08fdd0&count=250";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            Log.d(TAG, "onResponse: " + jsonData.length());
                            for (int x = 0; x <jsonData.length(); x++) {
                                if (jsonData.getJSONObject(x).get("type").equals("image")){
                                    allPhotos.add(jsonData.getJSONObject(x).getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse:  " + allPhotos.size());
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
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        instagramRecycler = view.findViewById(R.id.instagramRecycler);
        instagramRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
   //     instagramRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
     //   instagramRecycler.setLayoutManager(layoutManager);
        adapter = new DailyAdapter(getActivity(),allPhotos);
  //      instagramRecycler.setHasFixedSize(true);
        instagramRecycler.setAdapter(adapter);
        instagramRecycler.addOnScrollListener(new CenterScrollListener());

    }
}
