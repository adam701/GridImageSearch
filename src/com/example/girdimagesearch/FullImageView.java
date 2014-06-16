package com.example.girdimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FullImageView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_view);
		ImageResult imgResult = (ImageResult) getIntent().getSerializableExtra("img");
		SmartImageView screen = (SmartImageView) findViewById(R.id.smtImgFullImage);
		Toast.makeText(this, imgResult.getFullUrl(), Toast.LENGTH_SHORT).show();
		Log.d("DEBUG", imgResult.getFullUrl());
		screen.setImageUrl(imgResult.getFullUrl());
	}
}
