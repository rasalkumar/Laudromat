package com.example.saksham.laundromat.fragments.laundromat.fragments.Home;

/**
 * Created by USER on 05-02-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.saksham.laundromat.R;

/**
 * Created by Saksham on 26 Jul 2016.
 */
public class HomeViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;

    private Activity activity;

    public HomeViewHolders(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
    }
}
