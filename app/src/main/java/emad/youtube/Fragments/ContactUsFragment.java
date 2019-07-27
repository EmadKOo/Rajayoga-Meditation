package emad.youtube.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import emad.youtube.ContactUsActivity;
import emad.youtube.R;
import emad.youtube.Tools.RandomAPI;
import emad.youtube.VolleyUtils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    CircleImageView contactImage;
    TextView contactChannelName;
    TextView contactChannelDesc;
    TextView mailText;
    RelativeLayout emailLayout;
    ImageView faceLayout;
    ImageView youtubeLayout;
    ImageView instaLayout;
    Intent intent;

    RandomAPI randomAPI = new RandomAPI();
    String currentAPIKey = "";
    FirebaseAuth mAuth;

    private static final String TAG = "ContactUsFragment";

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact_us, container, false);

        currentAPIKey = randomAPI.getRandomKey();
        mAuth = FirebaseAuth.getInstance();
        initViews(view);
        handleViews();
        getChannelAbout();

        return view;
    }

    public void initViews(View view){
        contactImage = view.findViewById(R.id.contactImage);
        mailText = view.findViewById(R.id.mailText);
        contactChannelName = view.findViewById(R.id.contactChannelName);
        contactChannelDesc = view.findViewById(R.id.contactChannelDesc);
        emailLayout = view.findViewById(R.id.emailLayout);
        faceLayout = view.findViewById(R.id.faceLayout);
        instaLayout = view.findViewById(R.id.instaLayout);
        youtubeLayout = view.findViewById(R.id.youtubeLayout);
    }

    public void handleViews(){
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rajayogameditationonline@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android");
                startActivity(Intent.createChooser(intent, "Send Me Email"));
            }
        });

        youtubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/channel/UCAdN7ghumPNFA7Ww3vf_KpQ/featured"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/channel/UCAdN7ghumPNFA7Ww3vf_KpQ/featured"));
                    startActivity(intent);
                }
            }
        });

        instaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/RajayogaMeditation");
                intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setPackage("com.instagram.android");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/RajayogaMeditation")));
                }
            }
        });

        faceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = newFacebookIntent(getActivity().getPackageManager(), "https://www.facebook.com/rajayogameditationonline");
                startActivity(intent);
            }
        });

    }
    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public void getChannelAbout(){
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet&key="+ currentAPIKey +"&id=UCAdN7ghumPNFA7Ww3vf_KpQ";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("items");
                            try {
                                Picasso.get().load(jsonData.getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url")).placeholder(R.drawable.placeholder).into(contactImage);
                            }catch (Exception ex){
                                Picasso.get().load(getString(R.string.placeholder)).placeholder(R.drawable.placeholder).into(contactImage);
                            }

                            contactChannelName.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("title"));
//                            mailText.setText(jsonData.getJSONObject(0).getJSONObject("snippet").getString("title"));

                        }catch (Exception ex){
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: "+error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(getActivity()).addToRequestQueue(stringRequest);

    }
}
