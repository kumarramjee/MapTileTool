package com.mobimedia.locationmaptool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mobimedia.locationmaptool.R;
import com.mobimedia.locationmaptool.R.layout;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		Thread background = new Thread() {
			public void run() {

				try {
					sleep(1000);

					Intent i = new Intent(getBaseContext(),
							GridviewActivity.class);
					startActivity(i);
					finish();

				} catch (Exception e) {

				}
			}
		};

		// start thread
		background.start();

	}

}
