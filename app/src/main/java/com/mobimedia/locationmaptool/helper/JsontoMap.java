package com.mobimedia.locationmaptool.helper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mobimedia.locationmaptool.file.FileWriter;
import com.mobimedia.locationmaptool.model.Maptile;

public class JsontoMap {
	FileWriter fileWriter = new FileWriter();
	LatLngBounds bounds = null;
	Context context;
	int id;
	String bottomright;

	String topleft;
	String name = null;
	String rttop = null;
	BitmapDescriptor image;
	GoogleMap map;
	String bottomleft = null;
	List<Maptile> mtiledataset = new ArrayList<Maptile>();
	Bitmap bitmap;

	public JsontoMap() {
	}

	public List<Maptile> getAllTiles() {
		File file = new File(Environment.getExternalStorageDirectory(),
				FileWriter.DIR + "/" + FileWriter.FILE);
		String jsonData = fileWriter.readJson();
		try {
			FileInputStream stream = new FileInputStream(file);
			JSONArray jsonArr = null;
			jsonArr = new JSONArray(jsonData);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject obj = jsonArr.getJSONObject(i);
				Maptile mapTile = new Maptile();
				name = obj.getString("imageName");
				rttop = obj.getString("Toprightlatlong");
				bottomleft = obj.getString("Bottoleftlatlong");
				id = obj.getInt("imageId");
				topleft = obj.getString("Topleftlatlong");
				bottomright = obj.getString("Bottomrightlatlong");

				mapTile.imageId = id;
				mapTile.imageName = name;
				mapTile.topleftlatlong = topleft;
				mapTile.toprightlatlong = rttop;
				mapTile.bottomleftlatlong = bottomleft;
				mapTile.bottomnrightlatlong = bottomright;
				mtiledataset.add(mapTile);

				/*	Log.i("size of maptile data is", "," + mapTile.imageId);

				Log.i("JsonToMap", "TopRight latlonmg is:"
						+ mapTile.toprightlatlong
						+ ":Bottom left  latlonmg is:"
						+ mapTile.bottomleftlatlong + "image name is:" + name);*/
			}

			//Log.i("size of maptile data is", "," + mtiledataset.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mtiledataset;
	}

	public static List<Maptile> getAllTileslist() {
		JsontoMap getlatfromjson = new JsontoMap();
		List<Maptile> mapTileList = getlatfromjson.getAllTiles();
		return mapTileList;

	}

}
