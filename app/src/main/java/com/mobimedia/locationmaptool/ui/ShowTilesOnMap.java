package com.mobimedia.locationmaptool.ui;

import java.util.ArrayList;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.file.FileWriter;
import com.mobimedia.locationmaptool.helper.JsontoMap;
import com.mobimedia.locationmaptool.helper.OnSingleTapListener;
import com.mobimedia.locationmaptool.model.Maptile;
import com.mobimedia.locationmaptool.uihelper.MapBG;

public class ShowTilesOnMap extends ActionBarActivity implements OnGestureListener {
    MapBG mapBg = new MapBG(this);
    Bitmap bitmap;
    BitmapDescriptor image;
    LatLngBounds bounds;
    ImageView imageview;
    Context context;
    public ImageView img;
    public int position;
    RelativeLayout rl;
    ZoomControls zoom;
    public double lattitude;
    double longitude;
    public double toprtlatitude;
    public double toprtlongitude;
    public double bottomleftlatitude;
    public double bottomleftlongitude;
    public double bottomrtlatitude;
    public double bottomrtlongitude;
    ImageView imageView = null;
    Bitmap bmp = null;
    private Maptile mapTile = null;
    List<Maptile> mListMapTile;
    LatLng mapCentertoprt = null;
    LatLng mapCenterbtmleft = null;
    private LatLng cameraPos = null;
    private LayoutInflater inflator;
    GoogleMap map;
    String bottomleft = null;
    private OnSingleTapListener singleTapListener;
    GestureDetector gd;
    ArrayList<Maptile> mtiledataset = new ArrayList<Maptile>();
    FileWriter fileWriter = new FileWriter();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getall_tile);
        inflator = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        setupActionBar();
        getSupportActionBar().setBackgroundDrawable(null);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.alltilemap))
                .getMap();
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(false);

        mapBg.setFloorPlanBackgroundLayoutlist(map);
        if (mapTile != null && mapTile.topleftlatlong != null
                && mapTile.topleftlatlong.length() > 0) {
            cameraPos = MainActivity.getLatLong(mapTile.topleftlatlong);
        } else {
            cameraPos = new LatLng(28.304550, 76.977956);
        }

        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(cameraPos).zoom((float) 17.2).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar1 = getSupportActionBar();
        actionBar1.setDisplayShowHomeEnabled(false);
        actionBar1.setDisplayShowCustomEnabled(true);
        actionBar1.setDisplayShowTitleEnabled(false);
        View v = inflator.inflate(R.layout.actionbar_layoutforalltiles, null);
        TextView textedit = (TextView) v.findViewById(R.id.editimage);
        textedit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageOnMap();
                //	setupGestures();
            }
        });

        actionBar1.setCustomView(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImageOnMap() {
        Intent intent = new Intent(getApplicationContext(), AdjustAllTile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mtiledataset = (ArrayList<Maptile>) JsontoMap.getAllTileslist();
        intent.putParcelableArrayListExtra("list", mtiledataset);
        startActivity(intent);


    }

    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mtiledataset.size(); i++) {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 4;
            Maptile mapTile = mtiledataset.get(i);

            if (mapTile.imageName == null || mapTile.imageName.length() == 0)
                continue;
            if (mapTile.bottomleftlatlong == null || mapTile.bottomleftlatlong.equals("") || mapTile.toprightlatlong == null || mapTile.toprightlatlong.equals(""))
                continue;
            bitmap = mapBg.getBitmapFromAssets(mapTile);
            image = BitmapDescriptorFactory.fromBitmap(bitmap);
            bounds = new LatLngBounds(MainActivity.getLatLong(mapTile.bottomleftlatlong), MainActivity.getLatLong(mapTile.toprightlatlong));

        }
        imageview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub


                if (view instanceof ImageView) {
                    int intTag = (int) view.getTag();
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    int _xDelta = 0;
                    int _yDelta = 0;
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            _xDelta = X - lParams.leftMargin;
                            _yDelta = Y - lParams.topMargin;
                            Log.i("AdjustAllTile", "intTag   ACTION_DOWN== " + intTag);
                            break;
                        case MotionEvent.ACTION_UP:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            layoutParams.leftMargin = X - _xDelta;
                            layoutParams.topMargin = Y - _yDelta;
                            layoutParams.rightMargin = -view.getWidth();
                            layoutParams.bottomMargin = -view.getHeight();
                            int height = view.getHeight();
                            int width = view.getWidth();
                            Log.i("Adjust all tilex", " height and width is:" + height + "," + width);
                            int XX = layoutParams.leftMargin;
                            int YY = layoutParams.topMargin;
                            Point pointtopleft = new Point((XX), (YY));
                            LatLng mapCentertopleft = map.getProjection()
                                    .fromScreenLocation(pointtopleft);
                            lattitude = mapCentertopleft.latitude;
                            longitude = mapCentertopleft.longitude;
                            //Log.i("Adjust all Tiles on map", " Top left latitude is "
                            //		+ mapCentertopleft);
                            Point pointtr = new Point((XX + width), ((YY)));
                            LatLng mapCentertoprt = map.getProjection().fromScreenLocation(
                                    pointtr);
                            //Log.i("Adjust all Tiles on map", " Top right latitude is "
                            //		+ mapCentertoprt);
                            toprtlatitude = mapCentertoprt.latitude;
                            toprtlongitude = mapCentertoprt.longitude;
                            Point pointbl = new Point((XX), ((YY) + height));
                            LatLng mapCenterbtmleft = map.getProjection()
                                    .fromScreenLocation(pointbl);
                            Log.i("Adjust all Tiles on map", " Bottom left latitude is "
                                    + mapCenterbtmleft);
                            bottomleftlatitude = mapCenterbtmleft.latitude;
                            bottomleftlongitude = mapCenterbtmleft.longitude;
                            Point pointbtmrt = new Point((XX + width), ((YY) + height));
                            LatLng mapCenterbtmrt = map.getProjection().fromScreenLocation(
                                    pointbtmrt);
                            //	Log.i("Adjust all Tiles on map", " Bottom right latitude is "
                            //		+ mapCenterbtmrt);
                            bottomrtlatitude = mapCenterbtmrt.latitude;
                            bottomrtlongitude = mapCenterbtmrt.longitude;
                            Log.i("AdjustAllTile", "intTag  ACTION_UP == " + intTag);
                            Maptile mapTile = mListMapTile.get(intTag);
                            mapTile.bottomleftlatlong = ""
                                    + String.valueOf(bottomleftlatitude) + ","
                                    + String.valueOf(bottomleftlongitude);
                            mapTile.toprightlatlong = "" + String.valueOf(toprtlatitude)
                                    + "," + String.valueOf(toprtlongitude);
                            mapTile.bottomnrightlatlong = ""
                                    + String.valueOf(bottomrtlatitude) + ","
                                    + String.valueOf(bottomrtlongitude);
                            mapTile.topleftlatlong = "" + String.valueOf(lattitude) + ","
                                    + String.valueOf(longitude);
                            mListMapTile.set(intTag, mapTile);
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //	Log.i("AdjustAllTile", "intTag  ACTION_MOVE == " + intTag);
                            layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.leftMargin = X - _xDelta;
                            layoutParams.topMargin = Y - _yDelta;
                            layoutParams.rightMargin = -view.getWidth();
                            layoutParams.bottomMargin = -view.getHeight();
                            //	Log.i("Adjust all tiles ", "layoutParams.leftMargin == "+layoutParams.leftMargin+"  layoutParams.rightMargin == "+layoutParams.rightMargin+"   layoutParams.topMargin == "+layoutParams.topMargin+"  bottomMargib == "+layoutParams.bottomMargin);
                            view.setLayoutParams(layoutParams);
                            view.invalidate();
                            break;
                    }
                }


                return true;
            }
        });
    }

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        // TODO Auto-generated method stub
    }

    private void setupGestures() {
        gd = new GestureDetector((android.view.GestureDetector.OnGestureListener) this);
        //set the on Double tap listener
        gd.setOnDoubleTapListener(new OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (singleTapListener != null) {
                    return singleTapListener.onSingleTap(e);
                } else {
                    return false;
                }
            }


            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }
}
