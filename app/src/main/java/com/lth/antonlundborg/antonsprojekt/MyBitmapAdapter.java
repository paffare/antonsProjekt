package com.lth.antonlundborg.antonsprojekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyBitmapAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private LruCache<String, Bitmap> mMemoryCache;
    private int size;


    public MyBitmapAdapter(Context context, int size){
        this.size = size;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        inflater = LayoutInflater.from(context);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    @Override
    public int getCount() {
        return size;
    }

    public void addBitmap(Bitmap bitmap, int i){
        addBitmapToMemoryCache(Integer.toString(i), bitmap);
        notifyDataSetChanged();
    }
    public void decreseSize(){
        size--;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {

        return getBitmapFromMemCache(Integer.toString(position));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class ViewHolder {
        ImageView image;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.chartImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageBitmap((Bitmap)getItem(position));

        return convertView;
    }
}
