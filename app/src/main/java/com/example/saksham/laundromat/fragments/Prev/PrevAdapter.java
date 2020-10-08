package com.example.saksham.laundromat.fragments.Prev;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.saksham.laundromat.R;

import java.util.List;

/**
 * Created by USER on 08-02-2017.
 */

public class PrevAdapter extends RecyclerView.Adapter<PrevViewHolders> {
    private List<String> prevList;
    private Activity activity;

    public PrevAdapter(Activity activity, List<String> prevList) {
        this.prevList = prevList;
        this.activity = activity;
    }

    @Override
    public PrevViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_prev_item, null);
        PrevViewHolders holders = new PrevViewHolders(layoutView, activity);
        return holders;
    }

    @Override
    public void onBindViewHolder(PrevViewHolders holder, int position) {
        String s1 = prevList.get(position);
        holder.tvName.setText(s1);
    }




    @Override
    public int getItemCount() {
        return this.prevList.size();
    }

}
