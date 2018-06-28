package com.example.acer.album.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.acer.album.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract android.support.v4.app.Fragment newFragmentMonth();

    protected abstract void config();

    public abstract String GetTag();

    protected abstract Fragment newFragmentMap();

    protected abstract Fragment newFragmentMemory();

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private int currentFunctionNeedExcuse = 0;


    View container;
    private RenderScript rs = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // set toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        // show icon menu in actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Drawable d = new ColorDrawable(Color.argb(12,62,39,35));
        actionbar.setBackgroundDrawable(d);


        // get drawer
        drawerLayout = findViewById(R.id.drawer_layout);


        // event for drawer
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.nav_album:
                                Log.d("Album", "Album click");
                                break;
                            case R.id.nav_map:
                                Log.d("Map", "Map click");
                                currentFunctionNeedExcuse = 2; // map
                                if(isStoragePermissionGranted()){
//                                    if(isLocationPermission()){
//
//                                    }
                                    currentFunctionNeedExcuse = 0;
                                    LoadData loadDataMap = new LoadData(2);
                                    loadDataMap.execute();
                                }

                                break;
                            case R.id.nav_month:
                                Log.d("Month", "Month click");
                                currentFunctionNeedExcuse = 1; // month
                                if(isStoragePermissionGranted()){
                                    currentFunctionNeedExcuse = 0;
                                    // open fragment month
                                    LoadData loadDataMonth = new LoadData(1);
                                    loadDataMonth.execute();
                                }
                                break;
                            case R.id.nav_memory:
                                if(isStoragePermissionGranted()){
                                    currentFunctionNeedExcuse = 3; // memory
                                    // open fragment memory
                                    LoadData loadDataAlbum = new LoadData(3);
                                    loadDataAlbum.execute();
                                }
                                break;
                        }

                        return true;
                    }
                });

        container = findViewById(R.id.fragment_container);
        rs = RenderScript.create(this);

        // check permission
        currentFunctionNeedExcuse = 1; // month
        if(isStoragePermissionGranted()){
            currentFunctionNeedExcuse = 0;
            // open fragment month
            LoadData loadDataMonth = new LoadData(1);
            loadDataMonth.execute();
        }
    }


    public Bitmap captureView(View view, int width) {
        //Find the view we are after
        //Create a Bitmap with the same dimensions
        Bitmap image = Bitmap.createBitmap(width,
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality and remove opacity
        //Draw the view inside the Bitmap
        Canvas canvas = new Canvas(image);
        view.draw(canvas);

        //Make it frosty
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFe1f5fe, 0x00323232); // lighten
        //ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);

        return image;
    }

    public Bitmap createBlurBitmap(int width) {
        Bitmap bitmap = captureView(container, width);
        if (bitmap != null) {
            ImageHelper.blurBitmapWithRenderscript(rs, bitmap);
        }

        return bitmap;
    }

    public void OpenFragmentMonth(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("MONTH");

        if (fragment == null) {
            fragment = newFragmentMonth();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment, "MONTH");
            transaction.addToBackStack("MONTH");
            transaction.commit();
        }
        else{
            fragmentManager.popBackStackImmediate("MONTH", 0);
        }
    }

    public void OpenFragmentMemory(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("MEMORY");

        if (fragment == null) {
            fragment = newFragmentMemory();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment, "MEMORY");
            transaction.addToBackStack("MEMORY");
            transaction.commit();
        }
        else{
            fragmentManager.popBackStackImmediate("MEMORY", 0);
        }
    }

    public void OpenFragmentMap(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("GOOGLE_MAP");

        if (fragment == null) {
            fragment = newFragmentMap();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment, "GOOGLE_MAP");
            transaction.addToBackStack("GOOGLE_MAP");
            transaction.commit();
        }else{
            fragmentManager.popBackStackImmediate("GOOGLE_MAP", 0);
        }
    }

    public boolean isLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("MAP", "Permission get location is granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("MAP", "Permission get location is granted");
            return true;
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(GetTag(), "Permission read is granted");
                return true;
            } else {

                Log.v(GetTag(), "Permission read is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(GetTag(), "Permission read is granted");
            return true;
        }
    }

    public void setColorForToolbar(float number) {
        Drawable d = toolbar.getBackground();
        d.setAlpha((Math.round(number * 255)));
        toolbar.setBackground(d);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bitmap bitMapBlur = createBlurBitmap(navigationView.getWidth());
                navigationView.setBackground(new BitmapDrawable(getResources(), bitMapBlur));
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int n = permissions.length;
        for (int i = 0; i < n; i++) {
            if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(GetTag(), "Permission: " + permissions[i] + "was " + grantResults[i]);
                    if(currentFunctionNeedExcuse == 1){
                        currentFunctionNeedExcuse = 0;
                        // open fragment month
                        LoadData loadDataMonth = new LoadData(1);
                        loadDataMonth.execute();
                    }else if(currentFunctionNeedExcuse == 2){
                        currentFunctionNeedExcuse = 0;
                        LoadData loadDataMap = new LoadData(2);
                        loadDataMap.execute();
                    }else if(currentFunctionNeedExcuse == 3){
                        currentFunctionNeedExcuse = 0;
                        LoadData loadDataMemory = new LoadData(3);
                        loadDataMemory.execute();
                    }
                }
                break;
            }else if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            }
        }

    }

    public class LoadData extends AsyncTask<Object, Object, Object> {
        private int TypeOfFragment;

        public LoadData(int type){
            this.TypeOfFragment = type;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            config();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(this.TypeOfFragment == 1){ // month
                OpenFragmentMonth();
            }else if(this.TypeOfFragment == 2) { // map
                OpenFragmentMap();
            }else if(this.TypeOfFragment == 3){ // memory
                OpenFragmentMemory();
            }
        }
    }
}
