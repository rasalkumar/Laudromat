package com.example.saksham.laundromat.fragments.laundromat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saksham.laundromat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LostFragment extends Fragment {


    public LostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

}
