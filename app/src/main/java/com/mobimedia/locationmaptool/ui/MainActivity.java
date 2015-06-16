package com.mobimedia.locationmaptool.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.helper.MobiUtil;
import com.mobimedia.locationmaptool.model.Maptile;
import com.mobimedia.locationmaptool.parser.JsonWriter;

public class MainActivity extends ActionBarActivity implements OnTouchListener,
		OnMapLoadedCallback
		{
	public ImageView img;
	Context context;
	public int position;
	RelativeLayout rl;
	private int height = 0;
	private int width = 0;
	ZoomControls zoom;
	private GoogleMap map = null;
	public double lattitude;
	double longitude;
	public double toprtlatitude;
	public double toprtlongitude;
	public double bottomleftlatitude;
	public double bottomleftlongitude;
	private String name;
	public double bottomrtlatitude;
	public double bottomrtlongitude;
	private ViewGroup mRrootLayout;
	private int _xDelta;
	private int _yDelta;
	private ArrayList<Maptile> mListMapTile;
	private int mClickedItemId;
	private Maptile mapTile = null;
	ImageView imageView = null;
	Bitmap bmp = null;
	private ProgressDialog pd = null;
	LayoutInflater inflator;
	LatLng mapCentertoprt = null;
	LatLng mapCenterbtmleft = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		setupActionBar();
        getSupportActionBar().setBackgroundDrawable(null);
		mListMapTile = getIntent().getParcelableArrayListExtra("list");
		mClickedItemId = getIntent().getIntExtra("id", 0);
		mapTile = mListMapTile.get(mClickedItemId);
		mRrootLayout = (ViewGroup) findViewById(R.id.root);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.clear();
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.getUiSettings().setZoomControlsEnabled(false);

		camerafocusonmap();

		Intent i = getIntent();
		position = i.getExtras().getInt("id");
		imageView = (ImageView) findViewById(R.id.full_image_view);
		MobiUtil mobiUtil = new MobiUtil();
		bmp = mobiUtil.getBitmapFromAssets(mapTile, this);
		imageView.setImageBitmap(bmp);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				bmp.getWidth(), bmp.getHeight());
		imageView.setLayoutParams(layoutParams);
		imageView.setOnTouchListener(this);
		map.clear();
		map.setOnMapLoadedCallback(this);
		
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}

	public void camerafocusonmap() {
		LatLng cameraPos = null;

		if (mapTile != null && mapTile.topleftlatlong != null
				&& mapTile.topleftlatlong.length() > 0) {
			cameraPos = getLatLong(mapTile.topleftlatlong);
		} else {
			cameraPos = new LatLng(28.304550, 76.977956);
		}
		CameraPosition currentPlace = new CameraPosition.Builder()
				.target(cameraPos).zoom((float) 18.4).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
	}

	private void setupActionBar() {
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		View v = inflator.inflate(R.layout.actionbar_layout, null);
		TextView txtSave = (TextView) v.findViewById(R.id.counter3);
		txtSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveImageCoordinate();
			}
		});

		actionBar.setCustomView(v);
	}

	public boolean onTouch(View view, MotionEvent event) {
		imageOnTouch(view, event);
		mRrootLayout.invalidate();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.createactionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static LatLng getLatLong(String strLatLong) {
		String strLatiLongi[] = strLatLong.split(",");
		LatLng latLong = new LatLng(Double.parseDouble(strLatiLongi[0]),
				Double.parseDouble(strLatiLongi[1]));
		return latLong;
	}

	public void imageOnTouch(View view, MotionEvent event) {
		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:

			RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			_xDelta = X - lParams.leftMargin;
			_yDelta = Y - lParams.topMargin;
			System.out.println("XDELTA IS:" + _xDelta);
			System.out.println("YDELTA IS:" + _yDelta);
			break;
		case MotionEvent.ACTION_UP:
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.leftMargin = X - _xDelta;
			layoutParams.topMargin = Y - _yDelta;
			layoutParams.rightMargin = -imageView.getWidth();
			layoutParams.bottomMargin = -imageView.getHeight();
			height = imageView.getHeight();
			width = imageView.getWidth();
	
			int XX = layoutParams.leftMargin;
			int YY = layoutParams.topMargin;
			Point pointtopleft = new Point((XX), (YY));
			LatLng mapCentertopleft = map.getProjection().fromScreenLocation(
					pointtopleft);
				lattitude = mapCentertopleft.latitude;
			longitude = mapCentertopleft.longitude;

			Point pointtr = new Point((XX + width), ((YY)));
			LatLng mapCentertoprt = map.getProjection().fromScreenLocation(
					pointtr);
			System.out.println("latlong of top right point " + "is :"
					+ mapCentertoprt.latitude + " and " + "longitude is :"
					+ mapCentertoprt.longitude);
			toprtlatitude = mapCentertoprt.latitude;
			toprtlongitude = mapCentertoprt.longitude;

			Point pointbl = new Point((XX), ((YY) + height));
			LatLng mapCenterbtmleft = map.getProjection().fromScreenLocation(
					pointbl);
			bottomleftlatitude = mapCenterbtmleft.latitude;
			bottomleftlongitude = mapCenterbtmleft.longitude;

			Point pointbtmrt = new Point((XX + width), ((YY) + height));
			LatLng mapCenterbtmrt = map.getProjection().fromScreenLocation(
					pointbtmrt);
			bottomrtlatitude = mapCenterbtmrt.latitude;
			bottomrtlongitude = mapCenterbtmrt.longitude;

			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			
			layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			layoutParams.leftMargin = X - _xDelta;
			layoutParams.topMargin = Y - _yDelta;
			layoutParams.rightMargin = -imageView.getWidth();
			layoutParams.bottomMargin = -imageView.getHeight();
			view.setLayoutParams(layoutParams);
		//	Log.i("Adjust all tiles ", "layoutParams.leftMargin == "+layoutParams.leftMargin+"  layoutParams.rightMargin == "+layoutParams.rightMargin+"   layoutParams.topMargin == "+layoutParams.topMargin+"  bottomMargib == "+layoutParams.bottomMargin);
			break;
		}
	}

	@Override
	public void onMapLoaded() {
		pd.cancel();

		if (mapTile.topleftlatlong == null)
			return;
		if (mapTile.topleftlatlong.length() == 0)
			return;
		Projection projection = map.getProjection();
		Point point = projection
				.toScreenLocation(getLatLong(mapTile.topleftlatlong));
		float XX1 = point.x;
		float YY1 = point.y;
	
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView
				.getLayoutParams();
		layoutParams.leftMargin = (int) XX1;
		layoutParams.topMargin = (int) YY1;
		layoutParams.rightMargin = -imageView.getWidth();
		layoutParams.bottomMargin = -imageView.getHeight();
		imageView.setLayoutParams(layoutParams);
		mRrootLayout.invalidate();
	}

	private void saveImageCoordinate() {

		String lat = String.valueOf(lattitude);
		String longi = String.valueOf(longitude);
		String left = lat + "," + longi;

		String toprtlat = String.valueOf(toprtlatitude);
		String toprtlong = String.valueOf(toprtlongitude);
		String toprightlatlong = toprtlat + "," + toprtlong;
		;
		String botmlftlat = String.valueOf(bottomleftlatitude);
		String botmlftlong = String.valueOf(bottomleftlongitude);

		String bottomeftlatlong = botmlftlat + "," + botmlftlong;
		String bottomrtlat = String.valueOf(bottomrtlatitude);

		String bottomrtlong = String.valueOf(bottomrtlongitude);
		String bottomrtlatlong = bottomrtlat + "," + bottomrtlong;

		mapTile.topleftlatlong = left;
		mapTile.toprightlatlong = toprightlatlong;
		mapTile.bottomleftlatlong = bottomeftlatlong;
		mapTile.bottomnrightlatlong = bottomrtlatlong;
		mListMapTile.set(mClickedItemId, mapTile);

		com.mobimedia.locationmaptool.file.FileWriter fileWriter = new com.mobimedia.locationmaptool.file.FileWriter();
		JsonWriter jsonWriter = new JsonWriter();
		String data = jsonWriter.generataTileJson(mListMapTile);
		fileWriter.createFileOnSD(data);
		finish();
	}

}
