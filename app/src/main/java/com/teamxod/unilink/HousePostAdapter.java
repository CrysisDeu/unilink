package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

  public class HousePostAdapter extends BaseAdapter implements OnScrollListener {


      private ArrayList<HousePost> mList;
      private ImageLoader imageLoader;
      private ListView mListView;
      private LayoutInflater mInflater;
      private Context mContext;

      private int mStart;
      private int mEnd;
      private boolean isFirstIn;



      public HousePostAdapter(Context context, List<HousePost> objects, ListView listView){
          mList = (ArrayList<HousePost>) objects;
          mInflater=LayoutInflater.from(context);
          mListView = listView;
          isFirstIn = true;
          mContext = context;

          imageLoader = new ImageLoader(mListView);
          imageLoader.mUrls = new String[mList.size()];
          for(int i=0;i<mList.size();i++){
              imageLoader.mUrls[i] = mList.get(i).getImageResourceId();
          }
          mListView.setOnScrollListener(this);
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
      public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {


          ViewHolder viewHolder = null;
          if (view == null) {
              viewHolder = new ViewHolder();
              view = mInflater.inflate(R.layout.house_list_item,null);
              viewHolder.vhImage = (ImageView) view.findViewById(R.id.house_list_image);
              viewHolder.vhType = (TextView) view.findViewById(R.id.room_type);
              viewHolder.vhTitle = (TextView) view.findViewById(R.id.room_title);
              viewHolder.vhLocation = (TextView) view.findViewById(R.id.location);
              viewHolder.vhPrice = (TextView) view.findViewById(R.id.price);
              viewHolder.vhFavorate = (ToggleButton)view.findViewById(R.id.favorite_btn);
              view.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) view.getTag();
          }

          HousePost post = (HousePost)getItem(position);

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



         // viewHolder.vhImage.setTag(mList.get(position).getImageResourceId());
         // viewHolder.vhImage.setImageResource(R.drawable.my_bg);

          //imageLoader.showImageByAsyncTask(viewHolder.vhImage,mList.get(position).getImageResourceId());
          //Log.d("checkTag","**************");
          //imageLoader.showImage(viewHolder.vhImage, mList.get(position).getImageResourceId());


          viewHolder.vhType.setText(mList.get(position).getRoom_type());
          viewHolder.vhTitle.setText(mList.get(position).getRoom_title());
          viewHolder.vhPrice.setText("$"+mList.get(position).getRoom_price()+"/MO");
          viewHolder.vhLocation.setText(mList.get(position).getRoom_location());
          viewHolder.vhFavorate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  //add to favorite
                  HousePost post = (HousePost)getItem(position);
                  post.setFavorite(isChecked);
              }
          });

          viewHolder.vhFavorate.setChecked(post.isFavorite());
          return view;

      }

      class ViewHolder{
          ImageView vhImage;
          TextView vhType;
          TextView vhTitle;
          TextView vhPrice;
          TextView vhLocation;
          ToggleButton vhFavorate;
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
  }
