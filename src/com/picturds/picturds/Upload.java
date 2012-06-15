package com.picturds.picturds;


import java.io.File;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.picturds.picturds.CustomMultiPartEntity.ProgressListener;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class Upload extends AsyncTask<HttpResponse, Integer, String>
{
	ProgressDialog pd;
	long totalSize;
	String filename, email;
	Activity ac;
	private int update = 0;
	private Camera camera;
	String serverResponse = null;

	@Override
	protected void onPreExecute()
	{
		pd = new ProgressDialog(ac);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Uploading picture...");
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
			//multipartContent.addPart("email", new StringBody("haha@kvinnligarattigheter.nu"));
			//multipartContent.addPart("email", new StringBody(email));
			multipartContent.addPart("email", new StringBody(email));
			multipartContent.addPart("upload", new StringBody("Upload"));

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());
			
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
		pd.setProgress((int) progress[0]);
		setProgress((int) progress[0]);
		if(getProgress() == 100 && serverResponse != null) {
			pd.dismiss();
			Log.d("Done:", "response is not null!");
		}
	}


	@Override
	protected void onPostExecute(String ui)
	{
		pd.dismiss();
		camera.responseFromServer(trimQuotes(serverResponse));
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
	
	public void setObject(Camera camera) {
		this.camera = camera;
	}
	
	public int getProgress() {
		return update;
	}
	
	public void setProgress(int progress) {
		update = progress;
	}
	
	public static String trimQuotes( String value ) {
		if ( value == null )
	      return value;
	
	    value = value.trim( );
	    if ( value.startsWith( "\"" ) && value.endsWith( "\"" ) )
	      return value.substring( 1, value.length( ) - 1 );
	    
	    return value;
	}
}
