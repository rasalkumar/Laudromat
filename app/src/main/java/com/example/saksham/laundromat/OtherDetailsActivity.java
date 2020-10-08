package com.example.saksham.laundromat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OtherDetailsActivity";

    SharedPreferences sharedPreferences;

    String mobile;
    String idno;

    private EditText mMobileEditText;
    private EditText mIdnoEditText;

    TextInputLayout mobileInput;
    TextInputLayout idnoInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_details);

        sharedPreferences = getSharedPreferences(Config.SHARED_USER_DETAILS, Context.MODE_PRIVATE);

        mobileInput = (TextInputLayout) findViewById(R.id.mobile_layout);
        idnoInput = (TextInputLayout) findViewById(R.id.idno_layout);
        mMobileEditText = (EditText) findViewById(R.id.mobile);
        mIdnoEditText = (EditText) findViewById(R.id.idno);

        Button bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.bLogin) {
            mobile = mMobileEditText.getText().toString();
            idno = mIdnoEditText.getText().toString().toUpperCase();

            mobileInput.setErrorEnabled(false);
            idnoInput.setErrorEnabled(false);

            String errorMessage;
            if (!mobileValid(mobile)) {
                errorMessage = getString(R.string.error_mobile);
                mobileInput.setErrorEnabled(true);
                mobileInput.setError(errorMessage);
                return;
            }
            if (!idnoValid(idno)) {
                errorMessage = getString(R.string.error_code);
                idnoInput.setErrorEnabled(true);
                idnoInput.setError(errorMessage);
                return;
            }
            getData();
        }
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(OtherDetailsActivity.this, "", "Please Wait...", true);
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
                    URL url = new URL(Config.HOST + "auth/register.php");

                    String urlParams = "mobile=" + mobile +
                            "&idno=" + idno +
                            "&email=" + sharedPreferences.getString(Config.SP_EMAIL, "") +
                            "&name=" + sharedPreferences.getString(Config.SP_NAME, "");


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
            error = root.getBoolean("error");
            message = root.getString("message");

            if (!error) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.SP_LOGGED_IN, true);
                editor.putString(Config.SP_MOBILE, mobile);
                editor.putString(Config.SP_IDNO, idno);
                editor.commit();

                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Check your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean mobileValid(String mobile) {
        String MOBILE_REGEX = "^[0-9]{10}$";
        return mobile.matches(MOBILE_REGEX);
    }

    private static boolean idnoValid(String idno) {
        return idno.length() == 12;
    }
}
