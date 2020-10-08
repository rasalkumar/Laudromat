package com.example.saksham.laundromat.fragments.laundromat.fragments.Home;

/**
 * Created by USER on 05-02-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saksham.laundromat.R;

import java.util.List;

/**
 * Created by Saksham on 26 Jul 2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolders> {

    private List<Home> homeList;
    private Activity activity;

    public HomeAdapter(Activity activity, List<Home> homeList) {
        this.homeList = homeList;
        this.activity = activity;
    }

    @Override
    public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_item, null);
        HomeViewHolders holders = new HomeViewHolders(layoutView, activity);
        return holders;
    }

    @Override
    public void onBindViewHolder(HomeViewHolders holder, int position) {
        Home home = homeList.get(position);
        if (home.getImage() != null) {
            Log.d("aaa", position + "");
            Log.d("aaa", home.getImage().toString());
            holder.imageView.setImageBitmap(home.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return this.homeList.size();
    }
}
