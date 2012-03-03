package com.picturds.picturds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

//KOD TAGEN IFRÅN: http://www.anddev.org/networking-database-problems-f29/connecting-to-mysql-database-t50063.html

public class DBHandler {
	
	String result = null;
    InputStream is = null;
    StringBuilder sb=null;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	public DBHandler(){}
	
	public String getUser(String username, String password) {	
		nameValuePairs.add(new BasicNameValuePair("username",username));
		nameValuePairs.add(new BasicNameValuePair("password",password));
		
		//http post
		try{
			HttpClient httpclient = new DefaultHttpClient();
	   	    HttpPost httppost = new HttpPost("http://picturds.com/testkod/ans.php");
	   	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   	    HttpResponse response = httpclient.execute(httppost);
	   	    HttpEntity entity = response.getEntity();
	   	    is = entity.getContent();
	   	}catch(Exception e){
	   	     Log.e("log_tag", "Error in http connection"+e.toString());
	   	}
		
	   	//convert response to string
	   	try{
	   	     BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	   	     sb = new StringBuilder();
	   	     sb.append(reader.readLine() + "\n");
	   	     String line="0";
	   	     while ((line = reader.readLine()) != null) {
	   	    	 sb.append(line + "\n");
	   	     }
	   	     is.close();
	   	     result=sb.toString();
	   	     
	   	}catch(Exception e){
	   	        Log.e("log_tag", "Error converting result "+e.toString());
	   	}
	   	return result;
	}
}
