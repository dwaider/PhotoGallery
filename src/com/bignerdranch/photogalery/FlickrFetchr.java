package com.bignerdranch.photogalery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.util.Log;

//������� �������������� PhotoGallery ����� ������������-
//�� ����� ������� FlickrFetchr
//FlickrFetchr ����� �������� ����� �� ���� �������:
//	getUrlBytes(String) � getUrl(String).
public class FlickrFetchr {
	public static final String TAG = "FlickrFetchr";
	private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
	private static final String API_KEY = "f0f46515b3123cfc8a030566f0789ae3";
	private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
	private static final String PARAM_EXTRAS = "extras";
	private static final String EXTRA_SMALL_URL = "url_s";//url_s ����������� Flickr �������� URL 
	//����������� ������ �����������, ���� ��� ��������
	//��������	�������������� ������ �� URL � ���������� �� � ���� ������� ������.
	byte[] getUrlBytes(String urlSpec) throws IOException {
		//������� ������ URL �� ���� ������ � ��������, http://www.google.com
		URL url = new URL(urlSpec);
		//����� ������ openConnection() ������� ������ ����������� � ���������	URL-������.
		//����� URL.openConnection() ���������� URLConnection, �� ���������
		//����������� �������������� �� ��������� HTTP, �� ����� ������������� ���
		//� HttpURLConnection
		//��� ��������� ������ � HTTP-����������� ��� ������ � ��-
		//������ ��������, ������ �������, �������� ��������� �������� � �. �.
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		try {
			//����� � �������� ������
			//����� ����������� ������ ����� ������ getInputStream() (��� getOutputStream()
			//��� POST-�������). �� ����� ������� �� �� ������� �������� ��������������
			//��� ������.
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    InputStream in = connection.getInputStream();
		    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
		        return null;
		    }
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			//����� �������� ������� URL � �������� ����������� ��������� �����������
			//�������� read(), ���� � ����������� �� �������� ������. ������ in InputStream
			//������������� ����� �� ���� �� �����������. ����� ������ ����� ���������, ���-
			//������ ��������� ��� � ������ ������ ������ �� out ByteArrayOutputStream.
			while ((bytesRead = in.read(buffer)) > 0) {
			    out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
		    connection.disconnect();
		}
	}
	//������� ���� ������� ����� ������� ����� �� �������� ��-������� ������ �����������
	//����������� ��������� �� getUrlBytes(String) � String
	public String getUrl(String urlSpec) throws IOException {
		return new String(getUrlBytes(urlSpec));
	}
	
	public void fetchItems() {
		try {
			String url = Uri.parse(ENDPOINT).buildUpon()
			.appendQueryParameter("method", METHOD_GET_RECENT)
			.appendQueryParameter("api_key", API_KEY)
			.appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
			.build().toString();
  		    String xmlString = getUrl(url);
		    Log.i(TAG, "Received xml: " + xmlString);
		} catch (IOException ioe) {
		    Log.e(TAG, "Failed to fetch items", ioe);
		}
	}
}
