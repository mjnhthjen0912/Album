package com.example.acer.album.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.acer.album.models.Picture;
import com.github.chrisbanes.photoview.PhotoView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ShowImageAsyncTask extends AsyncTask<String, Object, Void> {
    ImageView imageView;

    int width;
    int heigth;
    Context context;
    String path;

    public ShowImageAsyncTask(ImageView imageView, int width, int height, String path, Context context){
        this.imageView = imageView;
        this.width = width;
        this.heigth = height;
        this.context = context;
        this.path = path;
    }


    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(Void bitmap) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.override(width, heigth);
        requestOptions.centerCrop();
        ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
        colorDrawable.setBounds(0,0,width, heigth);
        requestOptions.placeholder(colorDrawable);

        if(context != null){
            Glide.with(context)
                    .load(path)
                    .apply(
                            requestOptions
                    )
                    .transition(withCrossFade())
                    .into(imageView);
        }

    }


}
