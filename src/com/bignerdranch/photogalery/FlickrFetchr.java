package com.bignerdranch.photogalery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.util.Log;

//сетевые взаимодействия PhotoGallery будут обеспечивать-
//ся одним классом FlickrFetchr
//FlickrFetchr будет состоять всего из двух методов:
//	getUrlBytes(String) и getUrl(String).
public class FlickrFetchr {
	public static final String TAG = "FlickrFetchr";
	private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
	private static final String API_KEY = "f0f46515b3123cfc8a030566f0789ae3";
	private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
	private static final String PARAM_EXTRAS = "extras";
	private static final String EXTRA_SMALL_URL = "url_s";//url_s приказывает Flickr включить URL 
	//уменьшенной версии изображения, если она доступна
	//получает	низкоуровневые данные по URL и возвращает их в виде массива байтов.
	byte[] getUrlBytes(String urlSpec) throws IOException {
		//создает объект URL на базе строки — например, http://www.google.com
		URL url = new URL(urlSpec);
		//вызов метода openConnection() создает объект подключения к заданному	URL-адресу.
		//Вызов URL.openConnection() возвращает URLConnection, но поскольку
		//подключение осуществляется по протоколу HTTP, мы можем преобразовать его
		//в HttpURLConnection
		//Это открывает доступ к HTTP-интерфейсам для работы с ме-
		//тодами запросов, кодами ответов, методами потоковой передачи и т. д.
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		try {
			//связь с конечной точкой
			//будет установлена только после вызова getInputStream() (или getOutputStream()
			//для POST-вызовов). До этого момента вы не сможете получить действительный
			//код ответа.
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    InputStream in = connection.getInputStream();
		    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
		        return null;
		    }
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			//После создания объекта URL и открытия подключения программа многократно
			//вызывает read(), пока в подключении не кончатся данные. Объект in InputStream
			//предоставляет байты по мере их доступности. Когда чтение будет завершено, про-
			//грамма закрывает его и выдает массив байтов из out ByteArrayOutputStream.
			while ((bytesRead = in.read(buffer)) > 0) {
			    out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
		    connection.disconnect();
		}
	}
	//наличие двух методов будет полезно когда мы займемся за-грузкой данных изображений
	//преобразует результат из getUrlBytes(String) в String
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
