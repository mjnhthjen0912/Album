package com.example.acer.album.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Switch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Acer on 03/27/2018.
 */

public class Picture {
    private Integer id;
    private String name;
    private String path;
    private String album;
    private String month;
    private int width;
    private int height;
    private int ratio;
    private Float lat;
    private Float lng;

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Picture(Integer id, String name, String path, String album, String month,
                   int width, int height, Float lat, Float lng) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.album = album;
        this.month = month;
        this.width = width;
        this.height = height;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Picture(){

    }

    public static Bitmap resizeBitmap(String photoPath, int targetW, int targetH, int degree) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        if(bitmap != null){
            Log.d("ROTATE", "Bitmap khac null");
            Log.d("ROTATE", "Height rotate " + bmOptions.outHeight);
            Log.d("ROTATE", "Width rotate " + bmOptions.outWidth);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true);

        if(degree != 0){
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);

            bitmap = Bitmap.createBitmap(bitmap, 0,0, targetW, targetH, matrix, true);
        }

        return bitmap;
    }
}
