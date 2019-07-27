package emad.youtube.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import emad.youtube.Adapters.FavouriteAdapter;
import emad.youtube.FavouriteActivity;
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.R;
import emad.youtube.Tools.RegularFont;

public class FavouriteVideosFragment extends Fragment {

    RecyclerView favRecycler;
    ArrayList<Favourite> favouriteList = new ArrayList<>();
    FavouriteAdapter adapter;
    RegularFont noFav;

    FirebaseAuth mAuth;
    DatabaseReference favRef;
    Intent intent;

    private static final String TAG = "FavouriteVideosFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_videos, container, false);

        initRecyclerView(view);
        mAuth = FirebaseAuth.getInstance();
        getFavouriteListFromFirebase();

        return view;
    }

    public void initRecyclerView(View view){
        noFav = view.findViewById(R.id.noFav);
        favRecycler = view.findViewById(R.id.favRecycler);
        favRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavouriteAdapter(favouriteList, getActivity());
        favRecycler.setAdapter(adapter);
    }

    public void getFavouriteListFromFirebase(){
        if (mAuth.getCurrentUser()==null){
            noFav.setVisibility(View.VISIBLE);
            noFav.setText("Login To See Your Favourite Videos");
            favRecycler.setVisibility(View.GONE);
        }else {
            favRef = FirebaseDatabase.getInstance().getReference().child("fav").child(mAuth.getCurrentUser().getUid());
            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favouriteList.clear();
                    Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        favouriteList.add(snapshot.getValue(Favourite.class));
                    }

                    Log.d(TAG, "onDataChange: " + favouriteList.size());
                    if (favouriteList.size()==0)
                        noFav.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}