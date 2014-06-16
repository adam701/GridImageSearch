package com.example.girdimagesearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

public class GridSearch extends Activity {

	private EditText etQuery;
	private Button btnSearch;
	private GridView gvResult;
	private final List<ImageResult> imageResults = new ArrayList<>();
	private ImageResultArrayAdapter adapter;
	private final int REQUEST_CODE = 100;
	private final LinkedBlockingQueue<ImageResult> queue = new LinkedBlockingQueue<>();
	private SearchConfig searchConfig;
	private static final int PAGES = 8;
	private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?rsz="
			+ PAGES + "&v=1.0&";
	private static ObjectMapper mapper = new ObjectMapper();
	private String currentSearchQuery;
	private static final int LOAD_LOOP = 4;
	private EndlessScrollListener endlessScrollListener;

	static {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpViews();
		/*
		 * Thread loadImage = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { while (true) { ImageResult newImg; try
		 * { newImg = queue.take(); adapter.add(newImg); } catch
		 * (InterruptedException e) { Thread.currentThread().interrupt(); break;
		 * } } }
		 * 
		 * }); loadImage.start();
		 */
	}

	private void setUpViews() {
		this.etQuery = (EditText) findViewById(R.id.etQuery);
		this.btnSearch = (Button) findViewById(R.id.smtImgvFullImage);
		this.gvResult = (GridView) findViewById(R.id.gvResults);
		adapter = new ImageResultArrayAdapter(this, this.imageResults);
		this.gvResult.setAdapter(adapter);
		this.gvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageResult imgResult = imageResults.get(position);
				Intent i = new Intent(getApplicationContext(),
						FullImageView.class);
				i.putExtra("img", imgResult);
				startActivity(i);
			}
		});
		loadConfig();
		endlessScrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int nextPage) {
				Log.d("DEBUG", "On Scroll Listener!" + "nextPage is "
						+ nextPage);
				Log.d("DEBUG", "Begin to load image! Next Page is " + nextPage);
				loadImages(nextPage);
			}

		};
		this.gvResult.setOnScrollListener(endlessScrollListener);
	}

	public void onImageSearch(View v) {
		currentSearchQuery = this.etQuery.getText().toString();
		Log.d("DEBUG", "Current Search Query is " + currentSearchQuery);
		adapter.clear();
		Log.d("DEBUG", "adapter is cleared");
		loadImages(0);
	}

	public void loadImages(int nextPage) {
		if (currentSearchQuery == null
				|| "".equalsIgnoreCase(currentSearchQuery.trim())) {
			return;
		}
		Toast.makeText(this, "Search " + currentSearchQuery, Toast.LENGTH_SHORT)
				.show();
		// Google Image Search
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=barack
		AsyncHttpClient client = new AsyncHttpClient();
		// client.setTimeout(2000);
		int offset = nextPage * PAGES;
		Log.d("DEBUG", "Offset is " + offset);
		Toast.makeText(this, getQueryString(offset), Toast.LENGTH_SHORT).show();
		client.get(getQueryString(offset), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				try {
					JSONArray array = json.getJSONObject("responseData")
							.getJSONArray("results");
					adapter.addAll(ImageResult.toImageResultArray(array));
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void onSettings(MenuItem v) {
		Intent intent = new Intent(getApplicationContext(), Settings.class);
		intent.putExtra("config", this.searchConfig);
		startActivityForResult(intent, REQUEST_CODE);
	}

	private String getQueryString(final int offset) {
		String colorFilter = this.searchConfig != null ? this.searchConfig
				.getColorFilter() : null;
		String imageSize = this.searchConfig != null ? this.searchConfig
				.getImageSize() : null;
		String imageType = this.searchConfig != null ? this.searchConfig
				.getImageType() : null;
		String siteFilter = this.searchConfig != null ? this.searchConfig
				.getSiteFilter() : null;

		StringBuilder query = new StringBuilder();
		query.append(BASE_URL).append("q=")
				.append(Uri.encode(currentSearchQuery));
		if (colorFilter != null && !"".equals(colorFilter)
				&& "any".equalsIgnoreCase(colorFilter)) {
			query.append("&imgcolor=").append(Uri.encode(colorFilter));
		}
		if (imageSize != null && !"".equalsIgnoreCase(imageSize)
				&& !"any".equalsIgnoreCase(imageSize)) {
			query.append("&imgsz=").append(Uri.encode(imageSize));
		}
		if (imageType != null && !"".equalsIgnoreCase(imageType)
				&& !"any".equalsIgnoreCase(imageType)) {
			query.append("&imgtype=").append(Uri.encode(imageType));
		}
		if (siteFilter != null && !"".equalsIgnoreCase(siteFilter)) {
			query.append("&as_sitesearch=").append(Uri.encode(siteFilter));
		}
		query.append("&start=").append(offset);
		return query.toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE) {
				this.searchConfig = (SearchConfig) data
						.getSerializableExtra("config");
				Toast.makeText(this, "Return " + this.searchConfig.toString(),
						Toast.LENGTH_SHORT).show();
				saveConfig();
			}
		}
	}

	public void saveConfig() {
		try {
			File dirFile = this.getFilesDir();
			File settingFile = new File(dirFile, "setting.json");
			Toast.makeText(this, "Save " + this.searchConfig.toString(),
					Toast.LENGTH_SHORT).show();
			mapper.writeValue(settingFile, this.searchConfig);
		} catch (IOException e) {
			Log.d("DEBUG", "Fail to write setting.json");
			Toast.makeText(this, "Fail to Save" + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void loadConfig() {
		try {
			File dirFile = this.getFilesDir();
			File settingFile = new File(dirFile, "setting.json");
			this.searchConfig = mapper.readValue(settingFile,
					SearchConfig.class);
		} catch (IOException e) {
			Log.d("DEBUG", "Fail to load setting.json");
			this.searchConfig = new SearchConfig();
		}
	}
}
