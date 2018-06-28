package com.example.acer.album.activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;


import com.example.acer.album.models.PictureLab;

import java.io.IOException;

public class MainActivity extends SingleFragmentActivity {
    private String TAG = "MainActivity";

    @Override
    protected Fragment newFragmentMonth() {
        return new ParallaxRecyclerMonthFragment();
    }

    @Override
    protected void config() {
        try {
            PictureLab.getInstance(this).LoadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //List<Map<Integer, Picture>> list = new ArrayList<Map<Integer, Picture>>(PictureLab.getInstance(this).getPictureMonth().getMapPictures().values());
        //list = PictureLab.getInstance(this).getPictureMonth().getMapPictures().keySet().toArray(new String[0]);

        //int n = list.size();
        //for(int i = 0; i < n; i++){
        //    Log.d("MONTH", list.get(i).size() + " ");
        //}

//        String[] listF = new String[PictureLab.getInstance(this).getPictureAlbum().getMapPictures().keySet().size()];
//        listF = PictureLab.getInstance(this).getPictureAlbum().getMapPictures().keySet().toArray(new String[0]);
//
//        int nF = listF.length;
//        for(int k = 0; k < nF; k++){
//            Log.d("MONTH", listF[k]);
//        }
    }

    @Override
    public String GetTag() {
        return TAG;
    }

    @Override
    protected Fragment newFragmentMap() {
        return new GoogleMapFragment();
    }

    @Override
    protected Fragment newFragmentMemory() {
        return new MemoryFragment();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 1){
            finish();
        }
        super.onBackPressed();
    }
}
