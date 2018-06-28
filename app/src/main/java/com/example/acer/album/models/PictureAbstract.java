package com.example.acer.album.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class PictureAbstract {

    protected Map<String, Map<Integer, Picture>> pictures;

    public PictureAbstract(){
        pictures = new TreeMap<String, Map<Integer, Picture>>();
    }

    public Picture AddPicture(Picture picture) throws IOException {
        Map<Integer, Picture> target = pictures.get(getType(picture));
        if(target == null){
            target = new HashMap<Integer, Picture>();
            pictures.put(getType(picture), target);
        }

        return target.put(picture.getId(), picture);
    }


    public abstract String getType(Picture picture);

    public Map<String, Map<Integer, Picture>> getMapPictures(){
        return pictures;
    }

    public List<Picture> getListAllPicture(){
        ArrayList<Picture> list = new ArrayList<>();

        int n = this.pictures.size();

        this.pictures.forEach((k,v)->list.addAll(v.values()));

        return list;
    }

    public List<String> getListKey(){
        ArrayList<String> list = new ArrayList<>();
        this.pictures.forEach((k,v) -> list.add(k));
        return list;
    }

}
