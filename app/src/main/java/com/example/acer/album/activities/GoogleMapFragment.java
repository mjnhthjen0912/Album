package com.example.acer.album.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.acer.album.R;
import com.example.acer.album.models.Picture;
import com.example.acer.album.models.PictureLab;
import com.example.acer.album.models.StoreCluster;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private List<Picture> listPicture;
    private MapView mapView;
    public static Bitmap marker_buble;
    private ClusterManager<StoreCluster> mClusterManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listPicture = PictureLab.getInstance(getActivity()).getPictureMonth().getListAllPicture();
        marker_buble = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.ic_action_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Log.d("MAP", "create map");

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        MapFragment mapFragment = (MapFragment)(getActivity().getFragmentManager().findFragmentById(R.id.view_map));
//        mapFragment.getMapAsync(this);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

        Log.d("MAP", "create map2");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        mClusterManager = new ClusterManager<StoreCluster>(getActivity(), googleMap);
        CustomeClusterRender renderer = new CustomeClusterRender(getActivity(), googleMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomeInforViewAdapter(LayoutInflater.from(getActivity())));

        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

//        int n = listPicture.size();
//        for(int i = 0; i < n; i++){
//            if(listPicture.get(i).getLat() != null && listPicture.get(i).getLng() != null){
//                Picture picture = listPicture.get(i);
//                LatLng sydney = new LatLng(listPicture.get(i).getLat(), listPicture.get(i).getLng());
//                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(overlay(getCroppedBitmap(Picture.resizeBitmap(picture.getPath(), 150, 150, 0)),marker_buble));
//                //BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(overlay_2(marker_buble, getCroppedBitmap(Picture.resizeBitmap(picture.getPath(), 150, 150, 0))));
//
//                this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(bitmapDescriptor));
//                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                Log.d("GOOGLE_MAP", i+"");
//            }
//        }
        AddCluster();
        Log.d("MAP", "create map3");

    }

    public void AddCluster() {
        int n = listPicture.size();
        for (int i = 0; i < n; i++) {
            if (listPicture.get(i).getLat() != null && listPicture.get(i).getLng() != null) {
                Picture picture = listPicture.get(i);
                LatLng latLng = new LatLng(listPicture.get(i).getLat(), listPicture.get(i).getLng());
                //BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(overlay(getCroppedBitmap(Picture.resizeBitmap(picture.getPath(), 150, 150, 0)), marker_buble));

                mClusterManager.addItem(new StoreCluster(latLng, picture.getPath()));
            }
        }
        mClusterManager.cluster();
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
