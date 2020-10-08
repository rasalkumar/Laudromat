package com.example.saksham.laundromat.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;


import com.example.saksham.laundromat.Config;
import com.example.saksham.laundromat.R;
import com.example.saksham.laundromat.fragments.Home.Home;
import com.example.saksham.laundromat.fragments.Home.HomeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import com.example.user.laundrament.auth.LoginActivity;
//import com.example.user.user..fragments.home.Home;
//import com.example.saksham.gameon.fragments.fragments.events.EventsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public List<Home> homeList;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ProgressBar progressBar;


    public HomeFragment() {
        //Log.d("aaaaa", "const");
        homeList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);



        if (homeList.isEmpty()) {
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
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(Config.HOST + "home/get_home.php");
                    Log.d(TAG, url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    String s = sb.toString().trim();
                    return s;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    private void parseJSON(String json) {
        try {
            Log.d(TAG, json);
        } catch (Exception e) {
        }

        boolean error = true;
        String message = "";
        try {
            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray("home");
            error = root.getBoolean("error");
            message = root.getString("message");

            if (!error) {
                homeList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject j = array.getJSONObject(i);

                    Home home = new Home();
                    home.setFilename(j.getString("filename"));
                    home.setImage(null);
                    homeList.add(home);
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
        adapter = new HomeAdapter(getActivity(), homeList);
        recyclerView.setAdapter(adapter);

        for(int i=0; i<homeList.size(); i++){
            if (!homeList.get(i).getFilename().equals("")) {
                getBitmapFromURL(i);
            }
        }
    }

    public void getBitmapFromURL(final int i) {
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("error")) {
                    Toast.makeText(getActivity(), "download failed", Toast.LENGTH_SHORT).show();
                } else if (s.equals("redo")) {
                    getBitmapFromURL(i);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String filename = homeList.get(i).getFilename();
                String src = Config.HOST + "home/images/" + filename;
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    homeList.get(i).setImage(myBitmap);

                    try {
                        adapter.notifyItemChanged(i);
                    } catch (IllegalStateException e) {
                        return "redo";
                    }

                    return "success";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "error";
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }
}




