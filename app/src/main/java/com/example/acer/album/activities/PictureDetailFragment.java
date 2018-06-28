package com.example.acer.album.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.acer.album.R;
import com.example.acer.album.models.Picture;
import com.example.acer.album.models.PictureLab;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PictureDetailFragment extends Fragment {

    private static String ARG_PICTURE_ID = "com.example.acer.picture_id";
    private static String EXTRAC_RESULT = "extrac_result";
    private Picture picture;
    ImageView imageView;
    private static PhotoView photoView;
    PicturePaperActivity paperActivity;

    private int widthScreen;
    private int heigthScreen;

    public void getWithOfScreen(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heigthScreen = displayMetrics.heightPixels;
    }

    public static PictureDetailFragment newInstance(String id, String month){
        Bundle args = new Bundle();
        ArrayList<String> arr = new ArrayList<>(2);
        arr.add(id);
        arr.add(month);
        args.putStringArrayList(ARG_PICTURE_ID, arr);
        PictureDetailFragment pictureDetailFragment = new PictureDetailFragment();
        pictureDetailFragment.setArguments(args);
        return pictureDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picture = new Picture();
        paperActivity = (PicturePaperActivity) getActivity();

        ArrayList<String> arr = getArguments().getStringArrayList(ARG_PICTURE_ID);
        int id = Integer.parseInt(arr.get(0));
        picture = PictureLab.getInstance(getActivity()).getPictureMonth().getAPictureInMonth(arr.get(1), id);

    }

    public int getDegreeOfImage(String path){
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if(orientation == ExifInterface.ORIENTATION_NORMAL){

            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
                degree = 90;
            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
                degree = 180;
            }else{
                degree = 270;
            }


        } catch (IOException e) {
            Log.d("GET_DEGREE", e.getMessage());
            e.printStackTrace();
        }
        return degree;
    }

    public void SetImageForPhotoView(PhotoView photoView, Picture picture, int widthScreen, int heigthScreen){
        photoView.setId(picture.getId());


        int sourceWidth, sourceHeight;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(picture.getPath(), bmOptions);

        sourceWidth = bitmap.getWidth();
        sourceHeight = bitmap.getHeight();

        PictureLab.getInstance(getActivity()).getPictureMonth().getAPictureInMonth(picture.getMonth(), picture.getId()).setWidth(sourceWidth);
        PictureLab.getInstance(getActivity()).getPictureMonth().getAPictureInMonth(picture.getMonth(), picture.getId()).setHeight(sourceHeight);

        float ratio = sourceHeight * 1.0f / sourceWidth;
        int width, height;

        int subWidth = widthScreen - sourceWidth;
        int subHeight = heigthScreen - sourceHeight;


        if(subWidth <= 0 && subHeight <= 0){
            if(Math.abs(subWidth) - Math.abs(subHeight) > 0){
                width = widthScreen;
                height = Math.round(ratio * width);
            }else{
                height = heigthScreen;
                width = Math.round(height/ratio);
            }
        }else if(subHeight <= 0 & subWidth >= 0){
            height = heigthScreen;
            width = Math.round(height/ratio);
        }else if(subHeight >= 0 & subWidth <= 0 ){
            width = widthScreen;
            height = Math.round(ratio * width);
        }else{
            width = sourceWidth;
            height = sourceHeight;
        }


//        Picasso.get()
//                .load(new File(picture.getPath()))
//                .error(R.mipmap.ic_launcher)
//                .resize(width, height)
//                .into(photoView);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.override(width, height);
        requestOptions.fitCenter();
        requestOptions.placeholder(new ColorDrawable(Color.WHITE));
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(getActivity())
                .load(picture.getPath())
                .apply(
                        requestOptions
                )
                .into(photoView);

//        bitmap = Picture.resizeBitmap(picture.getPath(), width, height, getDegreeOfImage(picture.getPath()));
//        photoView.setImageBitmap(bitmap);
//        ShowImageAsyncTask showImageAsyncTask = new ShowImageAsyncTask(photoView, width, height, getDegreeOfImage(picture.getPath()));
//        showImageAsyncTask.execute(picture.getPath());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_detail, container, false);

        //imageView = view.findViewById(R.id.imageView);
        photoView = view.findViewById(R.id.photo_view);
        getWithOfScreen(getActivity());

        SetImageForPhotoView(photoView, picture, widthScreen, heigthScreen);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperActivity.ToolBarControl();
            }
        });

        // test ExifInterface
        ExifInterface exifReader = null;
        int orientation = 0;
        int rotate = 0;
        try {
            exifReader = new ExifInterface(picture.getPath());
            orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            // Do nothing. The original image is fine.
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            rotate = 90;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            rotate = 180;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            rotate = 270;
        }

        return view;
    }
}
