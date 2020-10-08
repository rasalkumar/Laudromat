package com.example.saksham.laundromat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "LoginActivity";

    private static final int RC_OTHER_DETAILS = 1;
    private static final int RC_SIGN_IN = 2;

    private GoogleApiClient mGoogleApiClient;

    private Button googleButton;

    SharedPreferences sharedPreferences;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(Config.SHARED_USER_DETAILS, Context.MODE_PRIVATE);

        googleButton = (Button) findViewById(R.id.google_button);
        googleButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();

        user = new User();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.google_button) {
            googleSignIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        String error = connectionResult.getErrorMessage();
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d("aaaaaa", "1");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == RC_OTHER_DETAILS) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("aaaaaa", "0");
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d("aaaaaa", result.getStatus().toString());
        if (result.isSuccess()) {
            Log.d("aaaaaa", "3");
            GoogleSignInAccount acct = result.getSignInAccount();

            String email = acct.getEmail();
            user.setEmail(email);
            user.setName(acct.getDisplayName());

            Log.d(TAG, user.getName());
            Log.d(TAG, email);

            if (email.length() == 33 && email.substring(8,33).equals("@pilani.bits-pilani.ac.in")) {
                getData(email);
            } else {
                Toast.makeText(LoginActivity.this, "Email not valid", Toast.LENGTH_SHORT).show();
                signOut();
            }

        } else {
            Log.d("aaaaaa", "4");
            String error = "error";
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
        }
    }

    private void getData(final String email) {
        class GetData extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LoginActivity.this, "", "Please Wait...", true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                int tmp;
                String data = "";
                try {
                    URL url = new URL(Config.HOST + "auth/login.php");
                    String urlParams = "email=" + email;

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    InputStream is = httpURLConnection.getInputStream();
                    while ((tmp = is.read()) != -1) {
                        data += (char) tmp;
                    }

                    is.close();
                    httpURLConnection.disconnect();

                    return data;

                } catch (IOException e) {
                    signOut();
                    e.printStackTrace();
                    return "error";
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    private void parseJSON(String json) {
        boolean error = true;
        String message = "";
        boolean exist;
        try {
            JSONObject root = new JSONObject(json);
            JSONObject j = root.getJSONObject("user_data");
            error = root.getBoolean("error");
            message = root.getString("message");
            exist = root.getBoolean("exist");

            if (!error) {
                if (exist) {
                    user.setMobile(j.getString("mobile"));
                    user.setName(j.getString("name"));
                    user.setEmail(j.getString("email"));
                    user.setIdNo(j.getString("idno"));

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Config.SP_LOGGED_IN, true);
                    editor.putString(Config.SP_EMAIL, user.getEmail());
                    editor.putString(Config.SP_MOBILE, user.getMobile());
                    editor.putString(Config.SP_NAME, user.getName());
                    editor.putString(Config.SP_IDNO, user.getIdNo());
                    editor.commit();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.SP_EMAIL, user.getEmail());
                    editor.putString(Config.SP_NAME, user.getName());
                    editor.commit();

                    Intent intent = new Intent(this, OtherDetailsActivity.class);
                    startActivityForResult(intent, RC_OTHER_DETAILS);
                }
            } else {
                signOut();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            signOut();
            e.printStackTrace();
            Toast.makeText(this, "Please Check your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }
}
