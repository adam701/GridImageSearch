package com.example.girdimagesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListView;

public class ImageResult implements Serializable{

	private static final long serialVersionUID = -1272247723369171997L;
	private final String fullUrl;
	private final String tbUrl;
	
	public ImageResult(final JSONObject json) throws JSONException{
		this.fullUrl = json.getString("url");
		this.tbUrl = json.getString("tbUrl");
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	
	@Override
	public String toString() {
		return "ImageResult [fullUrl=" + fullUrl + ", tbUrl=" + tbUrl + "]";
	}

	public String getTbUrl() {
		return tbUrl;
	}
	
	public static List<ImageResult> toImageResultArray(final JSONArray arr) throws JSONException{
		List<ImageResult> res = new ArrayList<>();
		for(int i = 0; i < arr.length(); i++){
			JSONObject jsonObj = arr.getJSONObject(i);
			res.add(new ImageResult(jsonObj));
		}
		return res;
	}
	
}
