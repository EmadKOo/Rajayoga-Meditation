package emad.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecurityActivity extends AppCompatActivity {

    TextView mMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        String msg = getIntent().getStringExtra("msg");
        mMsg = findViewById(R.id.mMsg);
        mMsg.setText(msg);
    }
}
