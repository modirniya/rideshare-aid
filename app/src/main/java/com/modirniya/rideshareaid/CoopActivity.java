package com.modirniya.rideshareaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class CoopActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    private String city_code, city_name;
    private FirebaseAuth mAuth;
    boolean flagSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        city_code = loadSetting("city_code", "N/A");
        city_name = loadSetting("city_name", "N/A");
        if (city_code.equals("N/A")) {
            Intent intent = new Intent(CoopActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_coop);
        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fbAuth();
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();

        Button btForum = findViewById(R.id.btForum);
        btForum.setOnClickListener(this);

        Button btVote = findViewById(R.id.btVote);
        btVote.setOnClickListener(this);

        Button btLogs = findViewById(R.id.btLogs);
        btLogs.setOnClickListener(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6771897198841555/5663003647");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        if (flagSelection) {
            changeFragment(new StatFragment(), city_code);
            flagSelection = false;
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btVote:
                flagSelection = true;
                changeFragment(new SelectionEntryFragment(), city_code);
                break;
            case R.id.btLogs:
                intent = new Intent(getApplicationContext(), LogsActivity.class);
                startActivity(intent);
                break;
            case R.id.btForum:
                intent = new Intent(getApplicationContext(), ForumActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void fbAuth() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);

        } else {
            TextView tvUsername = findViewById(R.id.tvUsername);
            tvUsername.setText(currentUser.getDisplayName());
            TextView tvCityName = findViewById(R.id.tvCityName);
            tvCityName.setText(city_name);
            changeFragment(new StatFragment(), city_code);
        }
    }

    public String loadSetting(String keyword, String def) {
        String result;
        if (def.equals("")) {
            result = "N/A";
        } else {
            result = def;
        }
        SharedPreferences settings = getSharedPreferences("Rideshare-Aid", 0);
        result = settings.getString(keyword, result);
        return result;
    }

    private void changeFragment(Fragment fragment, String code) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        if (fragment instanceof SelectionEntryFragment) {
            args.putString(SelectionEntryFragment.CITY_CODE, code);
        } else {
            args.putString(StatFragment.CITY_CODE, code);
        }
        fragment.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.commit();
    }

    void feedbackReceived() {
        changeFragment(new StatFragment(), city_code);
    }


}
