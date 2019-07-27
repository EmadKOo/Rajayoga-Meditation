package emad.youtube.Fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import emad.youtube.R;

public class OfflineFragment extends Fragment {

    private Cursor videocursor;
    private static final String TAG = "OfflineFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        getDownload();
        return view;
    }

    void findVideos(File dir, ArrayList<String> list){
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                findVideos(file, list);
            else if(file.getAbsolutePath().contains(".mp4"))
                list.add(file.getAbsolutePath());
        }
    }

    public ArrayList<String> getDownload() {
        HashSet<String> videoItemHashSet = new HashSet<>();
        String[] projection = { MediaStore.Video.VideoColumns.DATA
                ,MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Video.Media.DATA + " like ? ",
                new String[]{"%/" + "Rajayoga Meditation" + "/%"},
                null);
        try {
            cursor.moveToFirst();
            do{

                videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
                Log.d(TAG, "getDownload: "+cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Log.d(TAG, "getDownload2: "+cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
            }while(cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> downloadedList = new ArrayList<>(videoItemHashSet);
        return downloadedList;
    }
}
