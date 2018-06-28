package com.example.acer.album.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.acer.album.R;
import com.example.acer.album.models.Picture;
import com.example.acer.album.models.PictureLab;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PicturePaperActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Picture> mPicture;
    private static String EXTRA_PICTURE_ID = "com.example.acer.album.pictureid";
    private Toolbar myToolbar;
    private BottomNavigationView bottomNavigationView;
    private boolean isShowToolbar = true;
    private List<Integer> listDegree;
    private int widthScreen;
    private int heigthScreen;

    private int currentApiVersion;

    public static Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, PicturePaperActivity.class);
        intent.putExtra(EXTRA_PICTURE_ID, id);
        return intent;
    }

    public void getWithOfScreen(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heigthScreen = displayMetrics.heightPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_paper);

        getWithOfScreen(this);

        // set toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setColorForToolbar(0f);

        // transpare notificale bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        viewPager = findViewById(R.id.picture_view_pager);

        mPicture = PictureLab.getInstance(this).getPictureMonth().getListAllPicture();
        listDegree = new ArrayList<Integer>(Collections.nCopies(mPicture.size(), 0));

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Picture picture = mPicture.get(position);
                listDegree.set(position, 0);
                return PictureDetailFragment.newInstance(mPicture.get(position).getId()+"", mPicture.get(position).getMonth());
            }

            @Override
            public int getCount() {
                return mPicture.size();
            }
        });

        int id = getIntent().getIntExtra(EXTRA_PICTURE_ID, -1);

        int size = mPicture.size();
        for(int i = 0; i < size; i++){
            if(mPicture.get(i).getId().equals(id)){
                viewPager.setCurrentItem(i);
                break;
            }
        }

        Drawable d = new ColorDrawable(Color.argb(20,62,39,35));
        bottomNavigationView.setBackground(d);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_rotate_right:
                        Log.d("ROTATE", "rotate right");

                        if(isStoragePermissionGranted()){
                            ProcessRotate();
                        }

                        break;
                    case R.id.action_crop:
                        break;
                    case R.id.action_delete:
                        break;
                }
                return false;
            }
        });

    }

    public void ProcessRotate(){
        //PictureDetailFragment.RotatePhotoViewRight();
        //fragmentManager.getFragment()

        int idx = viewPager.getCurrentItem();
        Picture picture = mPicture.get(idx);
        PhotoView photoView = viewPager.findViewById(picture.getId());
        //Log.d("ROTATE", viewPager.getCurrentItem() + " current");
//        int currentDegree = 0;
//        if(photoView != null){
//            currentDegree = listDegree.get(idx);
//            currentDegree+=90;
//            listDegree.set(idx, currentDegree);
//            //photoView.animate().rotation(currentDegree).setInterpolator(new LinearInterpolator()).start();
//        }

        // update file image rotated in store
        File fileImage = new File(picture.getPath());
        int degree = RotateFileImageRight(fileImage);

        int sourceWidth, sourceHeight;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(picture.getPath(), bmOptions);

        sourceWidth = bitmap.getWidth();
        sourceHeight = bitmap.getHeight();

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

        Bitmap myBitmap = Picture.resizeBitmap(picture.getPath(), width, height, degree);
        photoView.setImageBitmap(myBitmap);
    }

    public int RotateFileImageRight(File file){
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(file.getPath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d("ROTATE", "ORIENTATION: " + orientation);
            if(orientation == ExifInterface.ORIENTATION_NORMAL){
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ""+ExifInterface.ORIENTATION_ROTATE_90);
                degree = 90;
                Log.d("ROTATE", "ROTATE: 90");
            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ""+ExifInterface.ORIENTATION_ROTATE_180);
                degree = 180;
                Log.d("ROTATE", "ROTATE: 180");
            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ""+ExifInterface.ORIENTATION_ROTATE_270);
                degree = 270;
                Log.d("ROTATE", "ROTATE: 270");
            }else{
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ""+ExifInterface.ORIENTATION_NORMAL);
                degree = 0;
                Log.d("ROTATE", "ROTATE: 0");
            }

            exifInterface.saveAttributes();
        } catch (IOException e) {
            Log.d("ROTATE", e.getMessage());
            e.printStackTrace();
        }
        return degree;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GetTag(),"Permission write is granted");
                return true;
            } else {

                Log.v(GetTag(),"Permission write is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(GetTag(),"Permission write is granted");
            return true;
        }
    }

    public String GetTag(){
        return "PicturePaperActivity";
    }

    public void setColorForToolbar(float number){
        Drawable d = myToolbar.getBackground();
        d.setAlpha((Math.round(number * 255)));
        myToolbar.setBackground(d);
    }

    public void ToolBarControl(){
        isShowToolbar = !isShowToolbar;
        if(isShowToolbar == false){
            hideToolbar();
        }else{
            showToolbar();
        }
    }

    public void hideToolbar(){
        myToolbar.animate().translationY(-myToolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()+100).setInterpolator(new AccelerateInterpolator()).start();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }

    public void showToolbar(){

        View decorView = getWindow().getDecorView();
        int uiOptions =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        myToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int n = permissions.length;
        for(int i = 0; i < n; i++){
            if(permissions[i].endsWith(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.v(GetTag(),"Permission: "+permissions[i]+ "was "+grantResults[i]);
                    //resume tasks needing this permission
                    ProcessRotate();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MENU", item.getItemId() + " Menu home press");
        switch (item.getItemId()){
            case android.R.id.home:

                onBackPressed();
                return true;
        }
        return false;
    }
}
