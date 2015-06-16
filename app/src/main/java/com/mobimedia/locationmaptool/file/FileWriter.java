package com.mobimedia.locationmaptool.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import android.os.Environment;
import android.util.Log;
public class FileWriter {
	public static final String DIR = "/LocationMapTool";
	public static final String FILE = "Maptile.json";

	public void createFileOnSD(String sBody) {
		String datatest = sBody;
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File(sdCard.getAbsolutePath() + DIR);
	//	Log.i("FileWriter", "path  == " + directory.getPath());
		directory.mkdirs();
		File file = new File(directory, FILE);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		OutputStreamWriter osw = new OutputStreamWriter(fOut);
		try {

			osw.write(datatest);
			osw.flush();
			osw.close();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isExist() {
		File sdCard = Environment.getExternalStorageDirectory();
		// File directory = new File(sdCard.getAbsolutePath() +DIR);
		File file = new File(sdCard.getAbsolutePath() + DIR + "/" + FILE);
	//	Log.i("FileWriter", "file  == " + file.getPath());
		if (file.exists())
			return true;
		return false;
	}

	public String readJson() {
		String jsonStr = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					FileWriter.DIR + "/" + FileWriter.FILE);
			FileInputStream stream = new FileInputStream(file);
			
			try {
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
						fc.size());
				jsonStr = Charset.defaultCharset().decode(bb).toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stream.close();
			}
	//		Log.i("Readjson", "jsonStr  == " + jsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
      return jsonStr;
	}
}
