package emad.youtube;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImage;
    CircleImageView channelImage;
    TextView splashTitle;
    TextView splashDesc;
    Button loginSplash;
    Button registerSplash;
    TextView skip;
    Intent intent ;
    private static final int TIME_SPLASH_CLOSE = 10000;//millis
    private static final int TIME_ANIMATION_DURATION = 3000;//millis
    private static final int TIME_ANIMATION_START = 500;//millis

    DatabaseReference myRef;
    String msg;
    private static final String TAG = "SplashActivity";
    FirebaseAuth mAuth;
    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        referenceViews();

        myRef = FirebaseDatabase.getInstance().getReference().child("security").child("msg");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msg = dataSnapshot.getValue().toString();
                if (msg.equals("Security")){
                  //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    if (mAuth.getCurrentUser()==null)
                        handleAuth();
                    else{
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }

                }else {
                    intent = new Intent(getApplicationContext(), SecurityActivity.class);
                    intent.putExtra("msg",msg);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void referenceViews(){
        splashImage = findViewById(R.id.splashImage);
        channelImage = findViewById(R.id.channelImage);
        splashTitle = findViewById(R.id.splashTitle);
        splashDesc = findViewById(R.id.splashDesc);
        loginSplash = findViewById(R.id.loginSplash);
        registerSplash = findViewById(R.id.registerSplash);
        skip = findViewById(R.id.skip);

        if (mAuth.getCurrentUser()!=null){
            loginSplash.setVisibility(View.GONE);
            registerSplash.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
        }
    }

    public void handleAuth(){
        loginSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth("Login");
            }
        });

        registerSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth("Register");
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    public void auth(String type){
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
//        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
//        if (mAuth.getCurrentUser()!=null){
//
//
//            animateFadeIN(splashDesc);
//            animateFadeIN(splashTitle);
//            animateFadeIN(splashImage);
//            animateFadeIN(channelImage);
//
//            mHandler.postDelayed(mRunnable, TIME_ANIMATION_DURATION + TIME_ANIMATION_START);
//
//        }
        super.onResume();
    }

    private void animateFadeIN(View imgv) {
        imgv.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setStartOffset(TIME_ANIMATION_START);
        fadeIn.setDuration(TIME_ANIMATION_DURATION);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        imgv.setAnimation(animation);
    }
}
