package com.mobimedia.locationmaptool.ui;

import java.io.BufferedInputStream;

import java.io.InputStream;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.model.Maptile;
import com.mobimedia.locationmaptool.parser.JsonWriter;

@SuppressLint({ "ServiceCast", "ShowToast", "NewApi" })
public class AdjustAllTile extends ActionBarActivity implements
		OnMapLoadedCallback, OnTouchListener, OnClickListener {
	private List<Maptile> mListMapTile;
	private GoogleMap map = null;
	private ProgressDialog pd = null;
	private Bitmap bitmap = null;
	private LayoutInflater inflater = null;
	private ViewGroup mRrootLayout;
	LatLng cameraPos = null;
	public ImageView img;
	Context context;
	public int position;
	RelativeLayout rl;
	private int height = 0;
	private int width = 0;
	ZoomControls zoom;
	public double lattitude;
	double longitude;
	public double toprtlatitude;
	public double toprtlongitude;
	public double bottomleftlatitude;
	public double bottomleftlongitude;
	private Maptile mapTile = null;
	public double bottomrtlatitude;
	public double bottomrtlongitude;
	private int _xDelta;
	private int _yDelta;
	Bitmap bmp = null;
	LayoutInflater inflator;
	private LatLng mapCentertoprt = null;
	private LatLng mapCenterbtmleft = null;
	private TextView savealltile;
	private ImageView smove;
	private ImageView im;
	private View mapview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjust_all_tile);
		inflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		setupActionBar();
		getSupportActionBar().setBackgroundDrawable(null);
		mListMapTile = getIntent().getParcelableArrayListExtra("list");
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		MapFragment mapFragment = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map));
		map.clear();
		mapview = mapFragment.getView();
		mRrootLayout = (ViewGroup) findViewById(R.id.root);
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.getUiSettings().setZoomControlsEnabled(false);
	    map.setOnMapLoadedCallback(this);
		LatLng cameraPos = null;
		if (mapTile != null && mapTile.topleftlatlong != null
				&& mapTile.topleftlatlong.length() > 0) {
			cameraPos = MainActivity.getLatLong(mapTile.topleftlatlong);
		}
		else 
		{
			cameraPos = new LatLng(28.304550, 76.977956);
		}
		CameraPosition currentPlace = new CameraPosition.Builder()
				.target(cameraPos).zoom((float) 17.2).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
		savealltile = (TextView) findViewById(R.id.savetile);
		savealltile.setOnClickListener(this);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
      	}
	@Override
	public void onMapLoaded() {
		pd.cancel();
		showImageOnMap();
		 map.setOnCameraChangeListener(new OnCameraChangeListener()
		 {
				@Override
				public void onCameraChange(CameraPosition arg0) {
					   showImageOnMap();
				}
			});
	}
	private void showImageOnMap() 
	{
		if(mRrootLayout != null && mRrootLayout.getChildCount() > 1)
		{
			mRrootLayout.removeViews(1, mRrootLayout.getChildCount()-1);
		}
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < mListMapTile.size(); i++) 
		{
			final Maptile mapTile = mListMapTile.get(i);
			if (mapTile.imageName == null || mapTile.imageName.length() == 0)
				continue;
			if (mapTile.bottomleftlatlong == null
					|| mapTile.bottomleftlatlong.equals("")
					|| mapTile.toprightlatlong == null
					|| mapTile.toprightlatlong.equals(""))
		      continue;
			bitmap = getBitmapFromAssets(mapTile);
			View v = inflater.inflate(R.layout.map_tile_image, null);
			ImageView imageview = (ImageView) v
					.findViewById(R.id.image_tile);
			imageview.setTag(i);
			Projection projection = map.getProjection();
			Point point = projection.toScreenLocation(MainActivity
					.getLatLong(mapTile.topleftlatlong));
			float XX1 = point.x;
			float YY1 = point.y;
			Log.i("AdjustAllTile", "i == "+i);
			imageview.setImageBitmap(bitmap);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageview
					.getLayoutParams();
			Log.i("AdjustAllTile", "mapTile.topleftlatlong == " + layoutParams);
			layoutParams.leftMargin = (int) XX1;
			layoutParams.topMargin = (int) YY1;
			layoutParams.rightMargin = -imageview.getWidth();
			layoutParams.bottomMargin = -imageview.getHeight();
			imageview.setLayoutParams(layoutParams);
			mRrootLayout.addView(v);
			mRrootLayout.invalidate();
			imageview.setOnTouchListener(this);
		}
	}
	public Bitmap getBitmapFromAssets(Maptile mapTile) {
		AssetManager assetManager = getAssets();
		InputStream istr = null;
		Bitmap bitmap = null;
		try 
		{
			istr = assetManager.open("bg/" + mapTile.imageName);
			bitmap = BitmapFactory.decodeStream(new BufferedInputStream(istr));
			if (istr != null)
				istr.close();
		} 
		catch (Exception e)
		{
		e.printStackTrace();	
		}
		return bitmap;
	}
	private void setupActionBar() {
		android.support.v7.app.ActionBar actionBar1 = getSupportActionBar();
		actionBar1.setDisplayShowHomeEnabled(false);
		actionBar1.setDisplayShowCustomEnabled(true);
		actionBar1.setDisplayShowTitleEnabled(false);
		View v = inflator.inflate(R.layout.action_on_adjustall, null);
		actionBar1.setCustomView(v);
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view instanceof ImageView) 
		{
			int intTag = (int) view.getTag();
			final int X = (int) event.getRawX();
			final int Y = (int) event.getRawY();
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
				layoutParams.leftMargin = X- _xDelta;
				layoutParams.topMargin = Y - _yDelta;
				layoutParams.rightMargin = -view.getWidth();
				layoutParams.bottomMargin = -view.getHeight();
				height = view.getHeight();
				width = view.getWidth();
				Log.i("Adjust all tilex", " height and width is:" + height+","+width);
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
	@Override
	public void onClick(View view) {
		if(view==savealltile)
		{
				saveImageCoordinates();
		}
	}
	private void saveImageCoordinates() {
				com.mobimedia.locationmaptool.file.FileWriter fileWriter = new com.mobimedia.locationmaptool.file.FileWriter();
		JsonWriter jsonWriter = new JsonWriter();
		String data = jsonWriter.generataTileJson(mListMapTile);
		fileWriter.createFileOnSD(data);
		finish();
	}
}
