package com.bignerdranch.photogalery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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
	private static final String XML_PHOTO = "photo";
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
	
	public ArrayList<GalleryItem> fetchItems() {
		ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
			String url = Uri.parse(ENDPOINT).buildUpon()
			.appendQueryParameter("method", METHOD_GET_RECENT)
			.appendQueryParameter("api_key", API_KEY)
			.appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
			.build().toString();
  		    String xmlString;
			try {
				xmlString = getUrl(url);
			    Log.i(TAG, "Received xml: " + xmlString);
			    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			    XmlPullParser parser = factory.newPullParser();
			    parser.setInput(new StringReader(xmlString));
			    parseItems(items, parser);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return items;
	}
	
	void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser)
		throws XmlPullParserException, IOException {
    		int eventType = parser.next();
	    	while (eventType != XmlPullParser.END_DOCUMENT) {
		          if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
		              String id = parser.getAttributeValue(null, "id");
		              String caption = parser.getAttributeValue(null, "title");
		              String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
		              GalleryItem item = new GalleryItem();
		              item.setmId(id);
		              item.setmCaption(caption);
		              item.setmUrl(smallUrl);
		              items.add(item);
		           }
		          eventType = parser.next();
		    }
	}
}
