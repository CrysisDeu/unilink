package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RoommateListAdapter extends BaseAdapter {

    private ArrayList<RoommateSimp> mList;
    private ListView mListView;
    private LayoutInflater mInflater;
    private Context mContext;

    public RoommateListAdapter(Context context, List<RoommateSimp> objects, ListView listView) {
        mList = (ArrayList<RoommateSimp>) objects;
        mInflater=LayoutInflater.from(context);
        mListView = listView;
        mContext = context;
    }

    class ViewHolder{

        ImageView vhPic;
        TextView vhName;
        TextView vhMajor;
        TextView vhYear;
        ProgressBar vhScore;

    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {


        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.roommate_list_item,null);
            viewHolder.vhPic = view.findViewById(R.id.headShot);
            Log.d("checknull","vhPic"+viewHolder.vhPic);
            viewHolder.vhName = view.findViewById(R.id.rName);
            Log.d("checknull","vhName"+viewHolder.vhName);
            viewHolder.vhMajor = view.findViewById(R.id.rMajor);
            viewHolder.vhScore = view.findViewById(R.id.rScore);
            viewHolder.vhYear = view.findViewById(R.id.rYear);

            view.setTag(viewHolder);
        } else {
            viewHolder = (RoommateListAdapter.ViewHolder) view.getTag();
        }

        RoommateSimp roommate = (RoommateSimp) getItem(position);

        //glide image
        if (roommate != null) {
            String url = roommate.getrPicture();
            Glide.with(mContext)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewHolder.vhPic);
        } else {
            viewHolder.vhPic.setImageDrawable(null);
        }

        //viewHolder.vhPic.setImageResource(R.drawable.ic_girl);
        viewHolder.vhName.setText(roommate.getrName());
        viewHolder.vhMajor.setText(roommate.getrMajor());
        viewHolder.vhYear.setText(roommate.getrYear()+"th Year");
        viewHolder.vhScore.setProgress(98);

        return view;

    }

}
