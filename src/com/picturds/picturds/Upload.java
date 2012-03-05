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
import org.apache.http.entity.mime.content.FileBody;
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
	
	public void executeMultipartPost(File bmp, String filename) throws Exception {
		String tag = "postFunction";

	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://picturds.com/upload.php");

	    try {
	        MultipartEntity entity = new MultipartEntity();

	        entity.addPart("file", new FileBody(bmp));
	        httppost.setEntity(entity);
	        HttpResponse response = httpclient.execute(httppost);
	        Log.i(tag, "picture was uploaded " + response.toString());

	    } catch (Exception e) {
			// handle exception here
			Log.e(e.getClass().getName(), e.getMessage());
		}
	}
}
