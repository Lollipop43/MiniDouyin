package com.example.minidouyin.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ImageHelper {
    public static void displayWebImage(String url, ImageView imageView, int width, int height) {
        RequestOptions options =
                new RequestOptions()
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide
             .with(imageView.getContext())
             .load(url)
             .apply(options)
             .into(imageView);
    }
}
