package com.example.acer.album.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Acer on 03/27/2018.
 */

public class PictureMonth extends PictureAbstract{

    public Map<Integer, Picture> getMapPictureInMonth(String month){
        return pictures.get(month);
    }

    public Picture getAPictureInMonth(String month, int id){
        return getMapPictureInMonth(month).get(id);
    }

    public List<Picture> getListPictureInMonth(String month) {
        return new ArrayList<>(getMapPictureInMonth(month).values());
    }

    @Override
    public String getType(Picture picture) {
        return picture.getMonth();
    }

}
