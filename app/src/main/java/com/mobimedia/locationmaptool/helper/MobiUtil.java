package com.mobimedia.locationmaptool.helper;
import java.io.BufferedInputStream;
import java.io.InputStream;
import com.mobimedia.locationmaptool.model.Maptile;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
public class MobiUtil 
  {
	 public Bitmap getBitmapFromAssets(Maptile mapTile, Context context) 
	   {
		   String strName=mapTile.imageName;
		   int id=mapTile.imageId;
		 //  Log.i("MapBG getBitmapFromAssets", "mapTile image name and id is:"+ strName+":"+id);
		
		   AssetManager assetManager=context.getAssets();
		
		InputStream istr = null;
		Bitmap bitmap = null;
		try {
		    istr = assetManager.open("bg/"+mapTile.imageName);
		     bitmap = BitmapFactory.decodeStream(new BufferedInputStream(istr));
		     //Log.i("MapBG", "bitmap == "+ bitmap.getWidth());
		     if(istr != null)
		 		istr.close();
		} catch (Exception e) {
		    Log.i("MapBG", "exception == "+e.getMessage());
		}
		
	    return bitmap;
	}
}
 