package com.mobimedia.locationmaptool.ui;
import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.adapter.ImageAdapter;
import com.mobimedia.locationmaptool.file.FetchAssetsFile;
import com.mobimedia.locationmaptool.file.FileWriter;
import com.mobimedia.locationmaptool.model.Maptile;
import com.mobimedia.locationmaptool.parser.JsonReader;
import com.mobimedia.locationmaptool.parser.JsonWriter;

public class GridviewActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private ListView listview;
	ImageView sendbutton;
	private ArrayList<Maptile> mListMapTile;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview);
		sendbutton = (ImageView) findViewById(R.id.sendmail);
		sendbutton.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.gridView);
		listview.setOnItemClickListener(this);
	TextView sendalltiles=(TextView)findViewById(R.id.tileimageshow);
	sendalltiles.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		Intent intent=new Intent(GridviewActivity.this,ShowTilesOnMap.class);
		startActivity(intent);
		}
	});
	}
	@Override
	protected void onResume() {
	super.onResume();
		createMapTiles();
		FileWriter fileWriter = new FileWriter();
		if (fileWriter.isExist()) {
			JsonReader jsonReader = new JsonReader();
			mListMapTile = jsonReader.loadTilesJsonFromFile();
		} else {

			JsonWriter jsonWriter = new JsonWriter();
			String data = jsonWriter.generataTileJson(mListMapTile);
			fileWriter.createFileOnSD(data);
		}
		ImageAdapter adapter = new ImageAdapter(GridviewActivity.this,mListMapTile);
		ListView listview = (ListView) findViewById(R.id.gridView);
		listview.setAdapter(adapter);
	}

	private void createMapTiles() {
		FetchAssetsFile fetchAssets = new FetchAssetsFile();
		String files[] = fetchAssets.getFiles(this);
		mListMapTile = new ArrayList<Maptile>();

		for (int i = 0; i < files.length; i++) {
			Maptile mapTile = new Maptile();
			mapTile.imageName = files[i];
			mapTile.setImageId(i);
			mListMapTile.add(mapTile);
		}
	}

	private void setImageOnMap(int position) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("id", position);
		intent.putParcelableArrayListExtra("list", mListMapTile);
		startActivity(intent);
	}

	private void sendFileToMail() {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_SUBJECT, "JSON File.");
		email.putExtra(Intent.EXTRA_TEXT, "Hi \n Please find the attched file.");
		email.setType("message/rfc822");
		File sdCard = Environment.getExternalStorageDirectory();
		email.putExtra(
				Intent.EXTRA_STREAM,
				Uri.parse("file:///" + sdCard.getPath() + "/" + FileWriter.DIR
						+ "/" + FileWriter.FILE));
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == sendbutton) {
			sendFileToMail();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		setImageOnMap(position);
	}
}
