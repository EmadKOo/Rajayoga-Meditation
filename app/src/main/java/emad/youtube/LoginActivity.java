package emad.youtube;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends BaseActivity {

    RelativeLayout googleSignIn;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    //String type;
    Button doAuth;
    EditText editTextLoginMail;
    EditText editTextLoginPassword;
    TextView typeAuth;
    ImageView imgBackIco;

    private static final String TAG = "GoogleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        initViews();
       /* type = getIntent().getStringExtra("type");
        if (type.equals("Register"))
            doAuth.setText("Register ");
        */
       handleAuth();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    public void initViews(){
        imgBackIco = findViewById(R.id.imgBackIco);
        typeAuth = findViewById(R.id.typeAuth);
        //typeAuth.setText(type);
        googleSignIn = findViewById(R.id.googleSignIn);
        doAuth = findViewById(R.id.doAuth);
        editTextLoginMail = findViewById(R.id.editTextLoginMail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        imgBackIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void handleAuth(){
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
       /* doAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("Register")){
                    if (!TextUtils.isEmpty(editTextLoginMail.getText().toString()) && !TextUtils.isEmpty(editTextLoginPassword.getText().toString())){
                        showProgressDialog();
                        mAuth.createUserWithEmailAndPassword(editTextLoginMail.getText().toString()
                                ,editTextLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideProgressDialog();
                                if (task.isSuccessful()){
                                    Log.d(TAG, "onComplete:Task Succeeded ");
                                    Toast.makeText(LoginActivity.this, "Register Done Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    Log.d(TAG, "onComplete:Task Failed ");
                                    Toast.makeText(LoginActivity.this, "Register Failed, Try Later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "You must Fill all Fields", Snackbar.LENGTH_SHORT).show();
                    }
                }else if (type.equals("Login")){
                    if (!TextUtils.isEmpty(editTextLoginMail.getText().toString()) && !TextUtils.isEmpty(editTextLoginPassword.getText().toString())){
                        showProgressDialog();
                        mAuth.signInWithEmailAndPassword(editTextLoginMail.getText().toString()
                                ,editTextLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hideProgressDialog();
                                if (task.isSuccessful()){
                                    Log.d(TAG, "onComplete:Task Succeeded ");
                                    Toast.makeText(LoginActivity.this, "Sign in Done Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    Log.d(TAG, "onComplete:Task Failed ");
                                    Toast.makeText(LoginActivity.this, "Sign in Failed, Try Later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "You must Fill all Fields", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });
*/
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching teh Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, autanticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWifGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with teh signed-in user's information
                            Log.d(TAG, "signInWifCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText("Google User  "+ user.getEmail());
            //          mDetailTextView.setText("Firebase User: " +  user.getUid());
            Log.d(TAG, "updateUI: "+ mAuth.getCurrentUser().getDisplayName());
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText("Signed Out");
//            mDetailTextView.setText(null);
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
