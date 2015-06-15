package com.lth.antonlundborg.antonsprojekt.Charts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lth.antonlundborg.antonsprojekt.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChartAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private String[] urlNumbers;
    private String urlStart;
    private String urlEnd;

    public ChartAdapter(Context context, String urlStart, String[] urlNumbers, String urlEnd) {
        this.inflater = LayoutInflater.from(context);
        this.urlStart = urlStart;
        this.urlNumbers = urlNumbers;
        this.urlEnd = urlEnd;
    }

    @Override
    public int getCount() {
        return urlNumbers.length;
    }

    @Override
    public String getItem(int position) {
        return urlStart + urlNumbers[position] + urlEnd;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View recycled, ViewGroup container) {
        final View imageLayout;
        if (recycled == null) {
            imageLayout = inflater.inflate(R.layout.row,
                    container, false);
        } else {
            imageLayout = recycled;
        }
        String url = getItem(position);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.chartImageView);
        ImageLoader.getInstance().displayImage(url, imageView);
        return imageView;
    }
}
