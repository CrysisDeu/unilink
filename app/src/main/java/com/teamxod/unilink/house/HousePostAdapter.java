package com.teamxod.unilink.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class HousePostAdapter extends BaseAdapter implements OnScrollListener {


    private ArrayList<HousePost> mList;
    private LayoutInflater mInflater;
    private Context mContext;


    HousePostAdapter(Context context, List<HousePost> objects) {
        mList = (ArrayList<HousePost>) objects;
        mInflater = LayoutInflater.from(context);
        mContext = context;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();
        view = mInflater.inflate(R.layout.house_list_item, null);
        viewHolder.vhImage = view.findViewById(R.id.house_list_image);
        viewHolder.vhType = view.findViewById(R.id.room_type);
        viewHolder.vhTitle = view.findViewById(R.id.room_title);
        viewHolder.vhLocation = view.findViewById(R.id.location);
        viewHolder.vhPrice = view.findViewById(R.id.price);
        viewHolder.vhFavorate = view.findViewById(R.id.favorite_btn);
        view.setTag(viewHolder);


        HousePost post = (HousePost) getItem(position);

        //glide image
        if (post != null) {
            String url = post.getImageResourceId();
            Glide.with(mContext)
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(viewHolder.vhImage);
        } else {
            viewHolder.vhImage.setImageDrawable(null);
        }

        String temp = "$" + mList.get(position).getRoom_price() + "/Mo  " + mList.get(position).getTerm();
        viewHolder.vhType.setText(mList.get(position).getRoom_type());
        viewHolder.vhTitle.setText(mList.get(position).getRoom_title());
        viewHolder.vhPrice.setText(temp);
        viewHolder.vhLocation.setText(mList.get(position).getRoom_location());

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final DatabaseReference favoriteReference =
                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(uid)
                        .child("favorite_houses");
        final String key = mList.get(position).getRoom_key();
        final ArrayList<String> favoriteList = new ArrayList<>();
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteList.clear();
                viewHolder.vhFavorate.setChecked(false);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String item = snapshot.getValue(String.class);
                    favoriteList.add(item);
                    if (key.equals(item)) {
                        viewHolder.vhFavorate.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        viewHolder.vhFavorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.vhFavorate.isChecked()) {
                    favoriteList.remove(key);
                    favoriteReference.setValue(favoriteList);
                } else {
                    favoriteList.add(key);
                    favoriteReference.setValue(favoriteList);
                }
            }
        });

        return view;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
          /*

          if(scrollState==SCROLL_STATE_IDLE){
              imageLoader.loadImages(mStart,mEnd);
          }else{
              imageLoader.cancelAllAsyncTask();
          }
          */

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

          /*mStart=firstVisibleItem;
          mEnd=firstVisibleItem+visibleItemCount-1;
          //Log.d("Tag","addBitmapToLrucaches"+visibleItemCount);
          if(isFirstIn&&visibleItemCount>0){
              //Log.d("checkTag","**************");
              imageLoader.loadImages(mStart,mEnd);
              isFirstIn=false;
          }*/

    }

    class ViewHolder {
        ImageView vhImage;
        TextView vhType;
        TextView vhTitle;
        TextView vhPrice;
        TextView vhLocation;
        ToggleButton vhFavorate;
    }
}
