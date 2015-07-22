package com.bignerdranch.photogalery;

public class GalleryItem {
	private String mCaption;
	private String mId;
	private String mUrl;
	public String toString() {
	   return mCaption;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmCaption(String mCaption) {
		this.mCaption = mCaption;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmId() {
		return mId;
	}
}
