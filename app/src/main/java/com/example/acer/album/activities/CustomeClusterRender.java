package com.example.akiyoshi.albumsole.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.example.acer.album.models.Picture;
import com.example.acer.album.models.StoreCluster;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomeClusterRender extends DefaultClusterRenderer<StoreCluster> {
    private Context mContext;

    public CustomeClusterRender(Context context, GoogleMap map, ClusterManager<StoreCluster> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(StoreCluster item, MarkerOptions markerOptions) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(overlay(getCroppedBitmap(Picture.resizeBitmap(item.getPathImage(), 150, 150, 0)), GoogleMapFragment.marker_buble));
        markerOptions.icon(bitmapDescriptor);
        markerOptions.draggable(true);
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(240, 240, bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        bmp2 = Bitmap.createScaledBitmap(bmp2, 240, 240, false);

        canvas.drawBitmap(bmp1, 44, 30, null);
        canvas.drawBitmap(bmp2, 0, 0, null);

//        canvas.drawBitmap(bmp1, new Matrix(), null);
//        canvas.drawBitmap(bmp2, -44, -30, null);
        return bmOverlay;
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
