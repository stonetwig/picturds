package com.picturds.picturds;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
 
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

//KOD TAGEN IFRÅN: http://www.anddev.org/networking-database-problems-f29/connecting-to-mysql-database-t50063.html

public class Upload {
	
	
	public Upload(){}
	
	public void executeMultipartPost(File bm) throws Exception {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = bos.toByteArray();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
					"http://picturds.com/upload.php");
			ByteArrayBody bab = new ByteArrayBody(data, "test.jpg");
			// File file= new File("/mnt/sdcard/forest.png");
			// FileBody bin = new FileBody(file);
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", bab);
			postRequest.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			System.out.println("Response: " + s);
			} catch (Exception e) {
				// handle exception here
				Log.e(e.getClass().getName(), e.getMessage());
			}
		}
}
