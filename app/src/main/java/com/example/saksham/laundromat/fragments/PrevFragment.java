package com.example.saksham.laundromat.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.saksham.laundromat.Config;
import com.example.saksham.laundromat.R;
import com.example.saksham.laundromat.fragments.Prev.Prev;
import com.example.saksham.laundromat.fragments.Prev.PrevAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrevFragment extends Fragment {

    private static final String TAG = "EventsFragment";

    SharedPreferences sharedPreferences;

    public static List<String> prevList;
    TextView textView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ProgressBar progressBar;


    public PrevFragment() {
        prevList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prev, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_USER_DETAILS, Context.MODE_PRIVATE);

        textView=(TextView)rootView.findViewById(R.id.tv1);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (prevList.isEmpty()) {
            getData();
        } else {
        showData();
        }

        return rootView;
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                parseJSON(s);
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(Config.HOST + "history/get_history.php");

                    String urlParams = "email=" + sharedPreferences.getString(Config.SP_EMAIL, "");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    StringBuilder sb = new StringBuilder();

                    OutputStream os = con.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    String s = sb.toString().trim();
                    return s;

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    int totalWashes;

    private void parseJSON(String json) {
        try {
            Log.d(TAG, json);
        } catch (Exception e) {
        }

        boolean error = true;
        String message = "";


        try {
            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray("history");

            error = root.getBoolean("error");
            message = root.getString("message");
            totalWashes = root.getInt("total");

            if (!error) {
                prevList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject j = array.getJSONObject(i);

                    String date = j.getString("date");
                    int iron = j.getInt("iron");
                    int status = j.getInt("status");

                    String text = "Washed on : " + date;
                    if (iron == 1) {
                        text += "\nIron : Yes";
                    } else {
                        text += "\nIron : No";
                    }
                    if (status == 0) {
                        text += "\nStatus : Pending";
                    } else {
                        text += "\nStatus : Delivered";
                    }
                    prevList.add(text);
                }
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void showData() {
        textView.setText("Washes remaining : " + (totalWashes-prevList.size()));
        adapter = new PrevAdapter(getActivity(), prevList);
        recyclerView.setAdapter(adapter);
    }

}
