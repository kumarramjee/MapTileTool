package com.mobimedia.locationmaptool.parser;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mobimedia.locationmaptool.file.FileWriter;
import com.mobimedia.locationmaptool.model.Maptile;
public class JsonReader {
	private ArrayList<Maptile> mListMapTile = new ArrayList<Maptile>();
	Context context;
	public JsonReader() {
	
	}
	public  ArrayList<Maptile> loadTilesJsonFromFile() 
	{
		mListMapTile = new ArrayList<Maptile>();
      FileWriter fileWriter = new FileWriter();
      String jsonData = fileWriter.readJson();
      if(jsonData == null)
    	  return mListMapTile;
      try {
		JSONArray jsonArr = new JSONArray(jsonData);
		for (int i = 0;i <jsonArr.length(); i++) {
			JSONObject obj = jsonArr.getJSONObject(i);
			Maptile maptile = new Maptile();
			
			String name=obj.getString("imageName");
	        int id=obj.getInt("imageId");
			String lefttop=obj.getString("Topleftlatlong");
			String rttop=obj.getString("Toprightlatlong");
			String bottomleft=obj.getString("Bottoleftlatlong");
			String bottomright=obj.getString("Bottomrightlatlong");
			    maptile.imageId=id;
				maptile.imageName=name;
				maptile.topleftlatlong=lefttop;
				maptile.toprightlatlong=rttop;
				maptile.bottomleftlatlong=bottomleft;
				maptile.bottomnrightlatlong=bottomright;
				mListMapTile.add(maptile);
	      
		}
		} catch (JSONException e) {
		e.printStackTrace();
	}
      return mListMapTile;
	}
}
