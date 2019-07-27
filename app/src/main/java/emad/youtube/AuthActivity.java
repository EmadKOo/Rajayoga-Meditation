package emad.youtube;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AuthActivity extends AppCompatActivity {

    ImageView imgBackIco;
    Button goToSignin;
    Button goToRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        imgBackIco = findViewById(R.id.imgBackIco);
        goToSignin = findViewById(R.id.goToSignin);
        goToRegister = findViewById(R.id.goToRegister);

        imgBackIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goToSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth("Login");
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth("Register");
            }
        });
    }

    public void auth(String type){
        Intent intent = new Intent(AuthActivity.this,LoginActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }
}
