package emad.youtube;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import emad.youtube.Helpers.DownloadImageHelper;

public class DisplayImageActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST = 7916;
    ImageView exitDisplayingImage;
    ImageView displayImage;
    TextView saveDisplayingImage;
    TextView shareDisplayingImage;

    int READ_STORAGE_PERMISSION = 7420;
    int WRITE_STORAGE_PERMISSION = 7430;
    String[] wStoragePermissions;
    String[] rStoragePermissions;
    Intent intent;
    private static final String TAG = "DisplayImageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        intent = getIntent();
        initViews();

        wStoragePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        rStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    }

    public void initViews(){
        exitDisplayingImage = findViewById(R.id.exitDisplayingImage);
        displayImage = findViewById(R.id.displayImage);
        saveDisplayingImage = findViewById(R.id.saveDisplayingImage);
        shareDisplayingImage = findViewById(R.id.shareDisplayingImage);

        try {
            Picasso.get().load(intent.getStringExtra("image")).placeholder(R.drawable.placeholder).into(displayImage);
        }catch (Exception ex){
            Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(displayImage);
        }

        shareDisplayingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkReadPermission())
                    shareIT();
                else
                    requestReadPermission();
            }
        });
        exitDisplayingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveDisplayingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
saveImage(intent.getStringExtra("image"));
            }
        });


    }

    private void saveImage(String path){
        if (ActivityCompat.checkSelfPermission(DisplayImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);
                Log.d(TAG, "saveImage: you should grant permission");
                return;
            }
        }else {
            AlertDialog dialog = new SpotsDialog.Builder().setContext(DisplayImageActivity.this).build();
            dialog.show();
            dialog.setMessage("Downloading ... ");

            String fileName = UUID.randomUUID().toString()+".jpg";
            Picasso.get().load(path)
                    .into(new DownloadImageHelper(findViewById(R.id.imageRoot),
                            dialog,
                            getApplicationContext().getContentResolver(),
                            fileName,
                            "image description"));


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode== STORAGE_PERMISSION_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage(intent.getStringExtra("image"));
                Toast.makeText(this, "Permission Granted Successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

        }else if (requestCode == WRITE_STORAGE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareIT();
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void shareIT(){
        Uri bmpUri = getLocalBitmapUri(displayImage);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Launch sharing dialog for image
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
    private boolean checkWritePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkReadPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestWritePermission() {
        ActivityCompat.requestPermissions(this, wStoragePermissions, READ_STORAGE_PERMISSION);
    }

    private void requestReadPermission() {
        ActivityCompat.requestPermissions(this, rStoragePermissions, WRITE_STORAGE_PERMISSION);
    }
}
