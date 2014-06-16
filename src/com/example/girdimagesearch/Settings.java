package com.example.girdimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Activity {

	private Spinner colorFilter;
	private Spinner imageSize;
	private Spinner imageType;
	private EditText siteFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Intent intent = getIntent();
		SearchConfig searchConfig = (SearchConfig) intent.getSerializableExtra("config");
		colorFilter = ((Spinner) findViewById(R.id.spColorFilter));
		imageSize = ((Spinner) findViewById(R.id.spImageSize));
		imageType = ((Spinner) findViewById(R.id.spImageType));
		siteFilter = ((EditText) findViewById(R.id.etSiteFilter));
		colorFilter.setSelection(searchConfig.getColorFilterID());
		imageSize.setSelection(searchConfig.getImageSizeID());
		imageType.setSelection(searchConfig.getImageTypeID());
		siteFilter.setText(searchConfig.getSiteFilter());
	}
	
	public void save(View v){
		Spinner colorFilter = ((Spinner) findViewById(R.id.spColorFilter));
		Spinner imageSize = ((Spinner) findViewById(R.id.spImageSize));
		Spinner imageType = ((Spinner) findViewById(R.id.spImageType));
		EditText siteFilter = ((EditText) findViewById(R.id.etSiteFilter));
		SearchConfig config = new SearchConfig(colorFilter, imageType, siteFilter, imageSize);
		Toast.makeText(this, config.toString(), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra("config", config);
		setResult(RESULT_OK, intent);
		finish();
	}
}
