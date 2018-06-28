package com.example.acer.album.models;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Acer on 03/27/2018.
 */

public class PictureLab {
    private static PictureLab pictureLab;
    private PictureMonth pictureMonth;
    private PictureAlbum pictureAlbum;
    private Context context;
    private String TAG = "PictureLab";

    public static PictureLab getInstance(Context context){
        if(pictureLab == null){
            pictureLab = new PictureLab(context);
        }
        return pictureLab;
    }

    private PictureLab(Context context){
        this.context = context;
        this.pictureMonth = new PictureMonth();
        this.pictureAlbum = new PictureAlbum();
    }

    public void LoadImage() throws IOException {
        if(this.pictureMonth.getListKey().size() == 0){
            Cursor mCursor = this.context.getContentResolver()
                    .query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            null,
                            null,
                            null,
                            MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            mCursor.moveToFirst();

            while(!mCursor.isAfterLast()) {
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                File file = new File(path);

                String album = file.getParent();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                int height = options.outHeight;
                int width = options.outWidth;

                String month = getMonth(file);
                String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                int id = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media._ID)));

                // get latlng
                ExifInterface exifInterface = new ExifInterface(path);
                float[] latlong = new float[2];

                Picture picture = null;

                if(exifInterface.getLatLong(latlong)){
                    picture = new Picture(id, name, path, album, month, width, height, latlong[0], latlong[1]);
                    Log.d("LATLNG", latlong[0] + " " + latlong[1]);
                }else{
                    picture = new Picture(id, name, path, album, month, width, height, null, null);
                }
                Log.d("LATLNG", "has1 " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                Log.d("LATLNG", "has2 " + exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
                Log.d("LATLNG", "has3 " + exifInterface.getAttribute(ExifInterface.TAG_GPS_STATUS));

                this.pictureMonth.AddPicture(picture);
                this.pictureAlbum.AddPicture(picture);
                mCursor.moveToNext();
            }
            mCursor.close();
        }
    }


    public String getMonth(File image) throws IOException {
        long createdAt = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            BasicFileAttributes attr = Files.readAttributes(image.toPath(), BasicFileAttributes.class);
            createdAt = attr.creationTime().toMillis();
        }else{
            createdAt= image.lastModified();
        }

        Calendar calendar = Calendar.getInstance();
        Date date = new Date(createdAt);
        calendar.setTime(date);

        StringBuilder stringBuilder = new StringBuilder();
        int month = (calendar.get(Calendar.MONTH)+1);
        switch (month) {
            case 1:
                stringBuilder.append("January");
                break;
            case 2:
                stringBuilder.append("February");
                break;
            case 3:
                stringBuilder.append("March");
                break;
            case 4:
                stringBuilder.append("April");
                break;
            case 5:
                stringBuilder.append("May");
                break;
            case 6:
                stringBuilder.append("June");
                break;
            case 7:
                stringBuilder.append("July");
                break;
            case 8:
                stringBuilder.append("August");
                break;
            case 9:
                stringBuilder.append("September");
                break;
            case 10:
                stringBuilder.append("October");
                break;
            case 11:
                stringBuilder.append("November");
                break;
            case 12:
                stringBuilder.append("December");
                break;
        };
        stringBuilder.append(", ").append(calendar.get(Calendar.YEAR));
        return stringBuilder.toString();
    }

    public PictureMonth getPictureMonth() {
        return pictureMonth;
    }

    public PictureAlbum getPictureAlbum() {
        return pictureAlbum;
    }
}
