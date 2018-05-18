package com.teamxod.unilink;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView myImageView;
    private String mUrl;
    private LruCache<String, Bitmap> mMemoryCache;

    public NewsAsyncTask(ImageView imageView, String url){
        myImageView = imageView;
        mUrl = url;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSizes = maxMemory/5;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSizes){
            @SuppressLint("NewApi") @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    //String...params是可变参数接受execute中传过来的参数
    @Override
    protected Bitmap doInBackground(String... params) {

        String url=params[0];
        //这里同样调用我们的getBitmapFromeUrl
        Bitmap bitmap = getBitmapFromUrl(params[0]);
        if (bitmap != null) {
            addBitmapToLrucache(url, bitmap);
        }
        return bitmap;
    }



    //这里的bitmap是从doInBackgroud中方法中返回过来的
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(myImageView.getTag().equals(mUrl)){
            myImageView.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL mUrl= new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap= BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void showImageByAsyncTask(ImageView imageView, String url){
        Bitmap bitmap = getBitmapFromLrucache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new NewsAsyncTask(imageView, url).execute(url);
        }
    }

    public Bitmap getBitmapFromLrucache(String url){
        return mMemoryCache.get(url);
    }

    public void addBitmapToLrucache(String url,Bitmap bitmap){
        if(getBitmapFromLrucache(url)==null){
            mMemoryCache.put(url,bitmap);
        }
    }
}
