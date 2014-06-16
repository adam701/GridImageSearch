package com.example.girdimagesearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.widget.EditText;
import android.widget.Spinner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchConfig implements Serializable {

	private static final long serialVersionUID = 4698401149620731048L;
	private final String colorFilter;
	private final int colorFilterID;
	private final String imageSize;
	private final int imageSizeID;
	private final String imageType;
	private final int imageTypeID;
	private final String siteFilter;

	public SearchConfig(final Spinner colorFilter, final Spinner imageType,
			final EditText siteFilter, final Spinner imageSize) {
		this.colorFilter = colorFilter.getSelectedItem().toString().trim();
		this.imageSize = imageSize.getSelectedItem().toString().trim();
		this.imageType = imageType.getSelectedItem().toString().trim();
		this.siteFilter = siteFilter.getText().toString().trim();
		this.colorFilterID = colorFilter.getSelectedItemPosition();
		this.imageSizeID = imageSize.getSelectedItemPosition();
		this.imageTypeID = imageType.getSelectedItemPosition();
	}

	@JsonCreator
	public SearchConfig(@JsonProperty("colorFilter") String colorFilter,
			@JsonProperty("colorFilterID") int colorFilterID,
			@JsonProperty("imageSize") String imageSize,
			@JsonProperty("imageSizeID") int imageSizeID,
			@JsonProperty("imageType") String imageType,
			@JsonProperty("imageTypeID") int imageTypeID,
			@JsonProperty("siteFilter") String siteFilter) {
		this.colorFilter = colorFilter;
		this.colorFilterID = colorFilterID;
		this.imageSize = imageSize;
		this.imageSizeID = imageSizeID;
		this.imageType = imageType;
		this.imageTypeID = imageTypeID;
		this.siteFilter = siteFilter;
	}

	public SearchConfig() {
		this.colorFilter = null;
		this.imageSize = null;
		this.imageType = null;
		this.siteFilter = "";
		this.colorFilterID = 0;
		this.imageSizeID = 0;
		this.imageTypeID = 0;
	}

	public int getColorFilterID() {
		return colorFilterID;
	}

	public int getImageSizeID() {
		return imageSizeID;
	}

	public int getImageTypeID() {
		return imageTypeID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getColorFilter() {
		return colorFilter;
	}

	public String getImageSize() {
		return imageSize;
	}

	public String getImageType() {
		return imageType;
	}

	public String getSiteFilter() {
		return siteFilter;
	}

	@Override
	public String toString() {
		return "SearchConfig [colorFilter=" + colorFilter + ", imageSize="
				+ imageSize + ", imageType=" + imageType + ", siteFilter="
				+ siteFilter + "]";
	}

}
