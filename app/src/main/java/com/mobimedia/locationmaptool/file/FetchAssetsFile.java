package com.mobimedia.locationmaptool.file;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
public class FetchAssetsFile 
{
public String[] getFiles(Context context){   
    String[] files = null; 
    try {  
        AssetManager assetManager = context.getAssets(); 
        

        files = assetManager.list( "bg"); 
        for (String string : files) {
		Log.i("FetchAssetsFile", "  string == "+string);
		}
        
    } catch (Exception e) {   
        e.printStackTrace();   
    }
	return files;
}
}
