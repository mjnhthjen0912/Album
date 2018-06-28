package com.example.acer.album.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class StoreCluster implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String mPathImage;
    private int mIdImage;

    public int getIdImage() {
        return mIdImage;
    }

    public void setIdImage(int mIdImage) {
        this.mIdImage = mIdImage;
    }

    public StoreCluster(LatLng position, String tile, String snippet){
        mPosition = position;
        mTitle = tile;
        mSnippet = snippet;
    }

    public StoreCluster(LatLng position, String path){
        mPosition = position;
        mPathImage = path;
    }

    public String getPathImage() {
        return mPathImage;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
