package emad.youtube.Tools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import emad.youtube.Iterfaces.IRequestPermission;
import emad.youtube.PlayVideoActivity;
import emad.youtube.R;

public class DialogHelper {
    String type;
    Dialog dialog;
    Context context;
    ImageView closeDialog;
    MontsFont allowPermission;
    Redhat otherTime;
    Redhat please;
    boolean result = false;
    IRequestPermission iRequestPermission;
    // type 0 for Fav & 1 for Storage
    public DialogHelper(Context context, IRequestPermission iRequestPermission){
        this.context = context;

        this.iRequestPermission = iRequestPermission;
    }
    public boolean displayDialog(String type){
        this.type = type;
        dialog =new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.req_permission);
        initViews(dialog);
        handleViews();
        dialog.show();
        return result;
    }

    public void hideDialog(){
        dialog.dismiss();
    }

    public void initViews(Dialog view){
        closeDialog = view.findViewById(R.id.closeDialog);
        please = view.findViewById(R.id.please);
        allowPermission = view.findViewById(R.id.allowPermission);
        otherTime = view.findViewById(R.id.otherTime);

        if (type.equals("0")){
            // fav
            please.setText("Please allow Permission to help us Adding Video To your Favourites");
        }else {
            please.setText("Please allow Permission to help us To Download Video");
        }
    }

    public void handleViews(){
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                result = false;
            }
        });

        otherTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                result = false;
            }
        });

        allowPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                if (type.equals("0")){
                    iRequestPermission.requestTelephone();
                }else {
                    iRequestPermission.requestStorage();
                }
                result = true;
            }
        });
    }
}