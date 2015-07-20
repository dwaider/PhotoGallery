package com.bignerdranch.photogalery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//������� �������������� PhotoGallery ����� ������������-
//�� ����� ������� FlickrFetchr
//FlickrFetchr ����� �������� ����� �� ���� �������:
//	getUrlBytes(String) � getUrl(String).
public class FlickrFetchr {
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
}
