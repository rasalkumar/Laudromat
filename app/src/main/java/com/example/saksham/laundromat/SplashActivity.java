package com.example.saksham.laundromat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(Config.SHARED_USER_DETAILS, Context.MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean(Config.SP_LOGGED_IN, false);

        //Log.d(TAG, ""+isLoggedIn);
        if (isLoggedIn) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        finish();
    }
}
