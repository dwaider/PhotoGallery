package com.bignerdranch.photogalery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//сетевые взаимодействия PhotoGallery будут обеспечивать-
//ся одним классом FlickrFetchr
//FlickrFetchr будет состоять всего из двух методов:
//	getUrlBytes(String) и getUrl(String).
public class FlickrFetchr {
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
}
