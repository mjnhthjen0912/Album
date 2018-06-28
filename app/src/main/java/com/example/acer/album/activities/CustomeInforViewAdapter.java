package com.example.akiyoshi.albumsole.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.acer.album.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomeInforViewAdapter implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater mInflater;

    public CustomeInforViewAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View popup = mInflater.inflate(R.layout.info_window_layout, null);

        //((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());


        return popup;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = mInflater.inflate(R.layout.info_window_layout, null);

        //((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());

        return popup;
    }
}
