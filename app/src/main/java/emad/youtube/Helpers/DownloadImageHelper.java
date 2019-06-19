package emad.youtube.Helpers;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class DownloadImageHelper implements Target {

    private View context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;

    public DownloadImageHelper(View context,AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc) {
        this.context = context;
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r = contentResolverWeakReference.get();
        AlertDialog dialog = alertDialogWeakReference.get();
        if (r!=null){
            MediaStore.Images.Media.insertImage(r,bitmap,name, desc);
        }
        dialog.dismiss();
       Snackbar.make(context, "Image Saved Successfully", Snackbar.LENGTH_LONG).show();


       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivity(Intent.createChooser(intent,"View Image"));*/
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
