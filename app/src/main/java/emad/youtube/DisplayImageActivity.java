package emad.youtube;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.UUID;

import dmax.dialog.SpotsDialog;
import emad.youtube.Helpers.DownloadImageHelper;

public class DisplayImageActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST = 7916;
    ImageView exitDisplayingImage;
    ImageView displayImage;
    TextView saveDisplayingImage;

    Intent intent;
    private static final String TAG = "DisplayImageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        intent = getIntent();
        initViews();
    }

    public void initViews(){
        exitDisplayingImage = findViewById(R.id.exitDisplayingImage);
        displayImage = findViewById(R.id.displayImage);
        saveDisplayingImage = findViewById(R.id.saveDisplayingImage);

        try {
            Picasso.get().load(intent.getStringExtra("image")).placeholder(R.drawable.placeholder).into(displayImage);
        }catch (Exception ex){
            Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(displayImage);
        }

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

        switch (requestCode){
            case STORAGE_PERMISSION_REQUEST:
            {

                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage(intent.getStringExtra("image"));
                    Toast.makeText(this, "Permission Granted Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}
