package com.bikroybaba.seller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bikroybaba.seller.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryImageAdapter extends BaseAdapter {

    private final Activity context;
    private final ArrayList<String> images;
    private final ArrayList<String> imageList;


    public GalleryImageAdapter(Activity localContext, ArrayList<String> images,
                               ArrayList<String> imageList) {
        context = localContext;
        this.images = images;
        this.imageList = imageList;
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_gridview, parent, false);

        ImageView imageView = convertView.findViewById(R.id.slider_image_view);
        Glide.with(context).load(images.get(position))
                .placeholder(R.drawable.ic_gallery).centerCrop()
                .into(imageView);
            for (String s : imageList) {
                if (s.equalsIgnoreCase(images.get(position))) {
                    convertView.findViewById(R.id.textViewID).setVisibility(View.VISIBLE);
                    convertView.findViewById(R.id.imageCheckIVId).setVisibility(View.VISIBLE);
                }
            }
        return convertView;
    }
}
