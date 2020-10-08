package com.example.saksham.laundromat.fragments.laundromat.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by USER on 09-02-2017.
 */
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
