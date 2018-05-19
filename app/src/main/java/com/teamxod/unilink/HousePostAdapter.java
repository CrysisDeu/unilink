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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*public class HousePostAdapter extends BaseAdapter implements AbsListView.OnScrollListener{


    private ArrayList<HousePost> mList;
    private ImageLoader imageLoader;//图片处理类，包含图片缓存 下载等
    private LayoutInflater inflater;
    private ListView mListView;

    private int start;// 第一张可见图片的下标
    private int end;// 最后一张可见图片的下标
    private boolean firstIn;//记录是否刚打开程序


    public HousePostAdapter(Context context,ArrayList<HousePost> data,ListView listView){

        mList=data;
        inflater=LayoutInflater.from(context);
        mListView=listView;
        firstIn = true;

        imageLoader=new ImageLoader(mListView);
        imageLoader.mUrls = new String[mList.size()];
        for(int i=0;i<mList.size();i++){
            imageLoader.mUrls[i] = mList.get(i).getImageResourceId();
        }
        mListView.setOnScrollListener(this);
    }
    @Override
    public int getCount(){
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

     /*   ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.house_list_item, null);
            viewHolder.vhImage = (ImageView) view.findViewById(R.id.image);
            viewHolder.vhType = (TextView) view.findViewById(R.id.room_type);
            viewHolder.vhTitle = (TextView) view.findViewById(R.id.room_title);
            viewHolder.vhLocation = (TextView) view.findViewById(R.id.location);
            viewHolder.vhPrice = (TextView) view.findViewById(R.id.price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.vhImage.setTag(mList.get(position).getImageResourceId());
        viewHolder.vhImage.setImageResource(R.drawable.my_bg);

        imageLoader.showImage(viewHolder.vhImage, mList.get(position).getImageResourceId());

        viewHolder.vhType.setText(mList.get(position).getRoom_type());
        viewHolder.vhTitle.setText(mList.get(position).getRoom_title());
        viewHolder.vhPrice.setText(mList.get(position).getRoom_price());
        viewHolder.vhLocation.setText(mList.get(position).getRoom_location());
        return view;
    }*/
        // Check if the existing view is being reused, otherwise inflate the view

  /*  class ViewHolder{
        ImageView vhImage;
        TextView vhType;
        TextView vhTitle;
        TextView vhPrice;
        TextView vhLocation;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadImages(start,end);
        }else{
            imageLoader.cancelAllAsyncTask();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        start=firstVisibleItem;
        end=firstVisibleItem+visibleItemCount;

        if(firstIn&&visibleItemCount>0){
            imageLoader.loadImages(start,end);
            firstIn=false;
        }

    }
}*/
  public class HousePostAdapter extends BaseAdapter implements OnScrollListener {

      private ArrayList<HousePost> mList;
      private ImageLoader imageLoader;
      private ListView mListView;
      private LayoutInflater mInflater;

      private int mStart;
      private int mEnd;
      private boolean isFirstIn;

      public HousePostAdapter(Context context, List<HousePost> objects, ListView listView){
          mList = (ArrayList<HousePost>) objects;
          mInflater=LayoutInflater.from(context);
          mListView = listView;
          isFirstIn = true;

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
      public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

          Log.d("Tag","addBitmapToLrucaches");
          ViewHolder viewHolder = null;
          if (view == null) {
              viewHolder = new ViewHolder();
              view = mInflater.inflate(R.layout.house_list_item,null);
              viewHolder.vhImage = (ImageView) view.findViewById(R.id.house_list_image);
              viewHolder.vhType = (TextView) view.findViewById(R.id.room_type);
              viewHolder.vhTitle = (TextView) view.findViewById(R.id.room_title);
              viewHolder.vhLocation = (TextView) view.findViewById(R.id.location);
              viewHolder.vhPrice = (TextView) view.findViewById(R.id.price);
              view.setTag(viewHolder);
          } else {
              viewHolder = (ViewHolder) view.getTag();
          }

          viewHolder.vhImage.setTag(mList.get(position).getImageResourceId());
         // viewHolder.vhImage.setImageResource(R.drawable.my_bg);

          //imageLoader.showImageByAsyncTask(viewHolder.vhImage,mList.get(position).getImageResourceId());
          //Log.d("checkTag","**************");
          imageLoader.showImage(viewHolder.vhImage, mList.get(position).getImageResourceId());


          viewHolder.vhType.setText(mList.get(position).getRoom_type());
          viewHolder.vhTitle.setText(mList.get(position).getRoom_title());
          viewHolder.vhPrice.setText(mList.get(position).getRoom_price());
          viewHolder.vhLocation.setText(mList.get(position).getRoom_location());
          return view;
          // Check if the existing view is being reused, otherwise inflate the view
         /* View listItemView = view;


          if(listItemView == null) {
              listItemView = View = LayoutInflater.from(getContext()).inflate(
                      R.layout.house_list_item, parent, false);
          }

          HousePost my_post= (HousePost) getItem(position);

          TextView typeTextView = (TextView) listItemView.findViewById(R.id.room_type);

          typeTextView.setText(my_post.getRoom_type());

          TextView titleTextView = (TextView) listItemView.findViewById(R.id.room_title);

          titleTextView.setText(my_post.getRoom_title());

          TextView priceTextView = (TextView) listItemView.findViewById(R.id.price);

          priceTextView.setText(my_post.getRoom_price());

          TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);

          locationTextView.setText(my_post.getRoom_location());

          ImageView imageImageView = (ImageView) listItemView.findViewById(R.id.image);

          imageImageView.setTag(my_post.getImageResourceId());

          ImageLoader imageLoader = new ImageLoader();

          imageLoader.showImageByAsyncTask(imageImageView,my_post.getImageResourceId());


          return listItemView;*/
      }

      class ViewHolder{
          ImageView vhImage;
          TextView vhType;
          TextView vhTitle;
          TextView vhPrice;
          TextView vhLocation;
      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {

          if(scrollState==SCROLL_STATE_IDLE){
              imageLoader.loadImages(mStart,mEnd);
          }else{
              imageLoader.cancelAllAsyncTask();
          }

      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem,
                           int visibleItemCount, int totalItemCount) {

          mStart=firstVisibleItem;
          mEnd=firstVisibleItem+visibleItemCount-1;
          //Log.d("Tag","addBitmapToLrucaches"+visibleItemCount);
          if(isFirstIn&&visibleItemCount>0){
              //Log.d("checkTag","**************");
              imageLoader.loadImages(mStart,mEnd);
              isFirstIn=false;
          }

      }
  }
