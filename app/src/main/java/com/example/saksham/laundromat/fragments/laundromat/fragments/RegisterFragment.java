package com.example.saksham.laundromat.fragments.laundromat.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saksham.laundromat.R;
import com.example.saksham.laundromat.fragments.laundromat.Config;
import com.example.saksham.laundromat.fragments.laundromat.fragments.plan.Plan;

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
public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private static boolean IS_REGISTERED;

    private RelativeLayout layout1;
    private TextView layout2;
    private Spinner spinner;
    private Button btnSubmit;

    ProgressBar progressBar;

    public static List<String> list = new ArrayList<>();

    public RegisterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_register, container, false);

        layout1 = (RelativeLayout) rootView.findViewById(R.id.layout1);
        layout2 = (TextView) rootView.findViewById(R.id.layout2);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        btnSubmit = (Button) rootView.findViewById(R.id.bSubmit);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = planList.get(spinner.getSelectedItemPosition()).getId();
                register(id);
            }
        });

        getData();

        return rootView;
    }

    private void setView() {
        if (IS_REGISTERED) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        } else {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
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
                    URL url = new URL(Config.HOST + "register/get_plan.php");

//                    String urlParams = "email=" + sharedPreferences.getString(Config.SP_EMAIL, "");
                    String urlParams = "email=" + "f2015245@pilani.bits-pilani.ac.in";

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

    Plan plan;
    List<Plan> planList = new ArrayList<>();


    private void parseJSON(String json) {
        try {
            Log.d(TAG, json);
        } catch (Exception e) {
        }

        boolean error = true;
        String message = "";


        try {
            JSONObject root = new JSONObject(json);

            error = root.getBoolean("error");
            message = root.getString("message");
            int plan_id = root.getInt("plan");

            if (!error) {
                if (plan_id == 0) {
                    IS_REGISTERED = false;

                    JSONArray array = root.getJSONArray("plans");

                    list.clear();
                    planList.clear();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);
                        Plan plan = new Plan();
                        plan.setId(j.getInt("id"));
                        plan.setWashes(j.getInt("washes"));
                        plan.setIron(j.getInt("iron"));
                        plan.setPrice(j.getInt("price"));
                        planList.add(plan);

                        String text = plan.getWashes() + " washes";
                        if (plan.getIron() == 1) {
                            text += " (iron)";
                        }
                        text += " Rs " + plan.getPrice();

                        list.add(text);
                    }

                    showData();

                } else {
                    IS_REGISTERED = true;
                    plan = new Plan();
                    plan.setId(plan_id);
                    plan.setWashes(root.getInt("washes"));
                    plan.setIron(root.getInt("iron"));
                    plan.setPrice(root.getInt("price"));
                    String text = "Registered for :\n" + plan.getWashes() + " washes";
                    if (plan.getIron() == 1) {
                        text += " (iron)";
                    }
                    text += " Rs " + plan.getPrice();
                    layout2.setText(text);
                }

                setView();

            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void register(final int id) {
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                parseJSON2(s);
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(Config.HOST + "register/register.php");

//                    String urlParams = "email=" + sharedPreferences.getString(Config.SP_EMAIL, "");
                    String urlParams = "email=" + "f2015245@pilani.bits-pilani.ac.in" +
                            "&id="+id;

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


    private void parseJSON2(String json) {
        try {
            Log.d(TAG, json);
        } catch (Exception e) {
        }

        boolean error = true;
        String message = "";


        try {
            JSONObject root = new JSONObject(json);

            error = root.getBoolean("error");
            message = root.getString("message");
            int plan_id = root.getInt("plan");

            if (!error) {
                IS_REGISTERED = true;
                plan = new Plan();
                plan.setId(plan_id);
                plan.setWashes(root.getInt("washes"));
                plan.setIron(root.getInt("iron"));
                plan.setPrice(root.getInt("price"));
                String text = "Registered for :\n" + plan.getWashes() + " washes";
                if (plan.getIron() == 1) {
                    text += " (iron)";
                }
                text += " Rs " + plan.getPrice();
                layout2.setText(text);

                setView();

            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
