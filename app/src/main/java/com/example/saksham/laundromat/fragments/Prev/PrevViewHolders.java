package com.example.saksham.laundromat.fragments.Prev;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.saksham.laundromat.R;


/**
 * Created by USER on 08-02-2017.
 */

public class PrevViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvName;

    private Activity activity;

    public PrevViewHolders(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        itemView.setOnClickListener(this);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
    }

    @Override
    public void onClick(View view) {
    }
}
