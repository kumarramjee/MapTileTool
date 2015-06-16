package com.mobimedia.locationmaptool.uihelper;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.mobimedia.locationmaptool.helper.JsontoMap;
import com.mobimedia.locationmaptool.helper.MobiUtil;
import com.mobimedia.locationmaptool.model.Maptile;
import com.mobimedia.locationmaptool.ui.MainActivity;

public class MapBG 
{
	
	private ViewGroup mRrootLayout;
		private GoogleMap map;	
		Context context;
		public Bitmap bitmap; 
		
		Maptile mapTile=new Maptile();
		BitmapDescriptor image;
		LatLngBounds bounds=null;
		
		JsontoMap js=new JsontoMap();
		MobiUtil mmobiutil=new MobiUtil();
		
		private List<Maptile> mtiledataset; 
	
	public MapBG(Context  context)
		 {
	     	this.context=context;
		 	mtiledataset=JsontoMap.getAllTileslist();
		 }
	
	
	public void setFloorPlanBackgroundLayoutlist(GoogleMap googleMap) 
    {
 		for(int i=0;i<mtiledataset.size();i++)
		{
 				BitmapFactory.Options o2 = new BitmapFactory.Options();
               o2.inSampleSize = 4;
             Maptile  mapTile = mtiledataset.get(i);
             
             if(mapTile.imageName ==  null || mapTile.imageName.length() == 0)
      	    	 continue;
          if(mapTile.bottomleftlatlong == null || mapTile.bottomleftlatlong.equals("") || mapTile.toprightlatlong == null || mapTile.toprightlatlong.equals(""))
      		 continue;
    	     bitmap = getBitmapFromAssets(mapTile);
      	     image = BitmapDescriptorFactory.fromBitmap(bitmap);
      	     bounds= new LatLngBounds(MainActivity.getLatLong(mapTile.bottomleftlatlong),MainActivity.getLatLong(mapTile.toprightlatlong));
  			 GroundOverlay mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions().image(image)
							.transparency(0.0f).visible(true).anchor(0, 1)
							.positionFromBounds(bounds));
	    mGroundOverlay.setTransparency(0f);
	    
	    if(bitmap!=null)
	    {
	    	bitmap.recycle();
	    	bitmap=null;
	     }
    }
	}
	 public Bitmap getBitmapFromAssets(Maptile mapTile) 
	   {
		   String strName=mapTile.imageName;
		   int id=mapTile.imageId;
	//	   Log.i("MapBG getBitmapFromAssets", "mapTile image name and id is:"+ strName+":"+id);
		
		   AssetManager assetManager=context.getAssets();
		
		InputStream istr = null;
		Bitmap bitmap = null;
		try {
		    istr = assetManager.open("bg/"+mapTile.imageName);
		     bitmap = BitmapFactory.decodeStream(new BufferedInputStream(istr));
		 //    Log.i("MapBG", "bitmap == "+ bitmap.getWidth());
		     if(istr != null)
		 		istr.close();
		} catch (Exception e) {
		   // Log.i("MapBG", "exception == "+e.getMessage());
		}
		
	    return bitmap;
	    
	}
	 
    
	
	}



