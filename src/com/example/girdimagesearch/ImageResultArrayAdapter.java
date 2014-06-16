package com.example.girdimagesearch;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> imageResults) {
		super(context, R.layout.image_item_view, imageResults);
	}

	//Translate method. Get data and convert it to view.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		ImageResult imgResult = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.image_item_view, parent, false);
		}
		// Lookup view for data population
		SmartImageView smtImageView = (SmartImageView) convertView;
		smtImageView.setImageResource(android.R.color.transparent);
		smtImageView.setImageUrl(imgResult.getTbUrl());
		
		// Return the completed view to render on screen
		return smtImageView;
	}

}
