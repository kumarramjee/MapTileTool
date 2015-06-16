package com.mobimedia.locationmaptool.parser;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobimedia.locationmaptool.file.FileWriter;
import com.mobimedia.locationmaptool.model.Maptile;

import android.util.Log;
public class JsonWriter 
{
public static String writeTilesJsonToFile(List<Maptile> listMaptile)
{
FileWriter fileWriter = new FileWriter();
    	JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		try {
			for (Maptile mapwritedata : listMaptile) {
				obj = new JSONObject();
    		obj.put("imageName",mapwritedata.imageName);
    		obj.put("imageId",mapwritedata.imageId);
			obj.put("topleftlatlong", mapwritedata.topleftlatlong);
			obj.put("toprightlatlong", mapwritedata.toprightlatlong);
			obj.put("bottoleftlatlong", mapwritedata.bottomleftlatlong);
			obj.put("bottomrightlatlong", mapwritedata.bottomnrightlatlong);
			arr.put(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
      if(arr != null && arr.length() > 0){
		if (fileWriter.isExist()) {
            
		}else{
			fileWriter.createFileOnSD(arr.toString());
		}
      }
	return arr.toString();
	}
	public String  generataTileJson(List<Maptile> listMaptile) {
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		try {
			for (Maptile maptile : listMaptile) {
				obj = new JSONObject();
			    obj.put("imageName",maptile.imageName);
			    obj.put("imageId", maptile.imageId);
			    obj.put("Topleftlatlong",getValue(maptile.topleftlatlong));
		 	    obj.put("Toprightlatlong", getValue(maptile.toprightlatlong));
		 		obj.put("Bottoleftlatlong",getValue(maptile.bottomleftlatlong));
		 		obj.put("Bottomrightlatlong",getValue(maptile.bottomnrightlatlong));
                    arr.put(obj);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		} 
		//Log.i("JsonWriter", "data == "+arr.toString());
     return arr.toString();
	}
	private String getValue(String value){
		String toBeReturn = "";
		toBeReturn = (value == null || value.equals(""))?"":value;
		return toBeReturn;
	}
}
