package emad.youtube.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import emad.youtube.Adapters.FavouriteAdapter;
import emad.youtube.FavouriteActivity;
import emad.youtube.Iterfaces.ChangeVideo;
import emad.youtube.Iterfaces.IRequestPermission;
import emad.youtube.Model.Favourite.Favourite;
import emad.youtube.Model.Latest.Latest;
import emad.youtube.Model.VideoList.ItemsListData;
import emad.youtube.R;
import emad.youtube.Tools.DialogHelper;
import emad.youtube.Tools.RegularFont;

public class FavouriteVideosFragment extends Fragment implements IRequestPermission {

    ChangeVideo changeVideo;
    RecyclerView favRecycler;
    ArrayList<Favourite> favouriteList = new ArrayList<>();
    FavouriteAdapter adapter;
    RegularFont noFav;

    FirebaseAuth mAuth;
    DatabaseReference favRef;
    Intent intent;

    String uniqueID = null;
    String telephonyPermission[];
    private static final int TELEPPHONY_REQUEST_CODE = 440;
    DialogHelper dialogHelper;
    private static final String TAG = "FavouriteVideosFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_videos, container, false);
        dialogHelper = new DialogHelper(getActivity(), this);
        initRecyclerView(view);
        mAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        telephonyPermission = new String[]{Manifest.permission.READ_PHONE_STATE};

        if (checkTelephonyPermission()){
            getID();
            adapter = new FavouriteAdapter(favouriteList, getActivity(), uniqueID);
            favRecycler.setAdapter(adapter);
            getFavouriteListFromFirebase();
        }
        else
            dialogHelper.displayDialog("0");
            //requestTelephonyPermission();
    }

    public void initRecyclerView(View view){
        noFav = view.findViewById(R.id.noFav);
        favRecycler = view.findViewById(R.id.favRecycler);
        favRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void getFavouriteListFromFirebase(){
//        if (mAuth.getCurrentUser()==null){
//            noFav.setVisibility(View.VISIBLE);
//            noFav.setText("Login To See Your Favourite Videos");
//            favRecycler.setVisibility(View.GONE);
//        }else {
        getID();
            favRef = FirebaseDatabase.getInstance().getReference().child("favs").child(uniqueID);
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
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        Log.d(TAG, "Emad : NOT onRequestPermissionsResult");

        if (requestCode == TELEPPHONY_REQUEST_CODE) {
            if (grantResults.length > 0) {
                Log.d(TAG, "Emad : NOT onRequestPermissionsResult 222");

                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    // add to favourites
                    Log.d(TAG, "onRequestPermissionsResult:fac ");
                  //  getID();

                } else {
                   // Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestTelephonyPermission() {
        ActivityCompat.requestPermissions(getActivity(), telephonyPermission, TELEPPHONY_REQUEST_CODE);
    }

    private boolean checkTelephonyPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }
    private void getID() {
        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) getActivity().getSystemService(ts);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
          return;
        }
        uniqueID = mTelephonyMgr.getSubscriberId();
        Log.d(TAG, "getID: imsi " + uniqueID);
        Log.d(TAG, "getID: imsi 2  "  + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public void notifyFavourites(){
        adapter = new FavouriteAdapter(favouriteList, getActivity(), uniqueID);
        favRecycler.setAdapter(adapter);
        getFavouriteListFromFirebase();
    }
    @Override
    public void requestTelephone() {
        requestTelephonyPermission();
        getID();
        if (uniqueID != null){
            Log.d(TAG, "Emad : NOT NUL");
            adapter = new FavouriteAdapter(favouriteList, getActivity(), uniqueID);
            favRecycler.setAdapter(adapter);
            getFavouriteListFromFirebase();
        }
    }

    @Override
    public void requestStorage() {
    }


}