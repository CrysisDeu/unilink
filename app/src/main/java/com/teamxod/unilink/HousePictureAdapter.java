package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class HousePictureAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Integer> images  = new ArrayList<Integer>();

    public HousePictureAdapter(Context context, House house) {
        this.context = context;
        //images = house.getHousePictures();
        images.add(R.drawable.my_bg3);
    }

    public HousePictureAdapter(Context context) {
        this.context = context;
        images.add(R.drawable.default_image);
        images.add(R.drawable.my_bg);
        images.add(R.drawable.my_bg2);
        images.add(R.drawable.my_bg3);
        images.add(R.drawable.my_bg4);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.house_picture, null);
        ImageView housePicture = (ImageView) view.findViewById(R.id.house_image);
        housePicture.setImageResource(images.get(position));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
