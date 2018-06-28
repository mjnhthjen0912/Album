package com.example.acer.album.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PictureAlbum extends PictureAbstract {

    public Map<Integer, Picture> getMapPictureInAlbum(String folder){
        return pictures.get(folder);
    }

    public Picture getPictureInAlbum(String folder, int id){
        return getMapPictureInAlbum(folder).get(id);
    }


    protected List<Picture> getListPicturesInAlbum(String argument) {
        return new ArrayList<>(getMapPictureInAlbum(argument).values());
    }

    @Override
    public String getType(Picture picture) {
        return picture.getAlbum();
    }
}
