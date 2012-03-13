package com.picturds.picturds;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.picturds.picturds.CustomMultiPartEntity.ProgressListener;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Upload extends AsyncTask<HttpResponse, Integer, String>
{
	ProgressDialog pd;
	long totalSize;
	String filename, email;
	Activity ac;

	@Override
	protected void onPreExecute()
	{
		pd = new ProgressDialog(ac);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Uploading Picture...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(HttpResponse... arg0)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://picturds.com/upload");

		try
		{
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new ProgressListener()
			{
				@Override
				public void transferred(long num)
				{
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			// We use FileBody to transfer an image
			multipartContent.addPart("userfile", new FileBody(new File(filename), filename, "image/jpeg", "utf-8" ));
			totalSize = multipartContent.getContentLength();
<<<<<<< HEAD
			multipartContent.addPart("email", new StringBody("haha@kvinnligarattigheter.nu"));
=======
			//multipartContent.addPart("email", new StringBody(email));
			multipartContent.addPart("email", new StringBody(email));
>>>>>>> anthuz/master
			multipartContent.addPart("upload", new StringBody("Upload"));

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			
			Log.i("SERVER", "UPLOADED: " + filename);
			Log.i("SERVER", "Response: " + response.toString());
			Log.i("SERVER", "Response: " + serverResponse);
			return serverResponse;
		}

		catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String ui)
	{
		pd.dismiss();
	}
	
	public void setFilename(String name){
		this.filename = name;
	}
	
	public void setEmail(String mail){
		this.email = mail;
	}
	
	public void setActivity(Activity activity) {
		this.ac = activity;
	}
}
