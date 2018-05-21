package com.teamxod.unilink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
public class ImageLoader {


    private LruCache<String, Bitmap> mMemoryCaches;
    private Set<NewsAsyncTask> mTasks;
    private ListView mListView;

    public String mUrls[];

    public ImageLoader(ListView listView) {


        this.mListView = listView;
        mTasks = new HashSet<>();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSizes = maxMemory / 5;

        mMemoryCaches = new LruCache<String, Bitmap>(cacheSizes) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }

    public Bitmap getBitmapFromLrucache(String url) {

        return mMemoryCaches.get(url);
    }

    public void addBitmapToLrucaches(String url, Bitmap bitmap) {

        if (getBitmapFromLrucache(url) == null) {
            mMemoryCaches.put(url, bitmap);
        }

    }

    public void loadImages(int start, int end) {

        for (int i = start; i < end; i++) {

            String loadUrl = mUrls[i];
            if (getBitmapFromLrucache(loadUrl) != null && (ImageView) mListView.findViewWithTag(loadUrl)!=null) {

                ImageView imageView = (ImageView) mListView.findViewWithTag(loadUrl);

                imageView.setImageBitmap(getBitmapFromLrucache(loadUrl));
            } else {
                NewsAsyncTask mNewsAsyncTask = new NewsAsyncTask(loadUrl);
                mTasks.add(mNewsAsyncTask);
                mNewsAsyncTask.execute(loadUrl);
            }
        }
    }

    public void showImage(ImageView imageView, String url) {

        Bitmap bitmap = getBitmapFromLrucache(url);
        if (bitmap == null) {
            imageView.setImageResource(R.drawable.grey_rect);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void cancelAllAsyncTask() {
        if (mTasks != null) {
            for (NewsAsyncTask newsAsyncTask : mTasks) {
                newsAsyncTask.cancel(false);
            }
        }

    }


    public Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL mUrl = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) mUrl
                    .openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
            new NewsAsyncTask(url).execute(url);
        }
    }

    class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {

        //private ImageView myImageView;
        private String mUrl;

        public NewsAsyncTask(String url){
            //myImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String url = params[0];
            Bitmap bitmap;

            bitmap = getBitmapFromUrl(url);


            if (bitmap != null) {

                addBitmapToLrucaches(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);

            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }

            mTasks.remove(this);
        }

    }

}
