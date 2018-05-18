package com.teamxod.unilink;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {

   /* int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int cacheSizes = maxMemory/5;


    private LruCache<String,Bitmap> mMemoryCaches= new LruCache<String, Bitmap>(cacheSizes){
        @SuppressLint("NewApi")
        @Override

        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };


    public void showImageByAsyncTask(ImageView imageView, String url){
        Bitmap bitmap = getBitmapFromLrucache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new NewsAsyncTask(imageView, url).execute(url);
        }
    }

    public Bitmap getBitmapFromLrucache(String url){
        return mMemoryCaches.get(url);
    }

    public void addBitmapToLrucache(String url,Bitmap bitmap){
        if(getBitmapFromLrucache(url)==null){
            mMemoryCaches.put(url,bitmap);
        }
    }*/

}
