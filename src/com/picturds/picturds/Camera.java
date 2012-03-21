package com.picturds.picturds;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Camera extends Activity implements View.OnClickListener{
	Button bUpload, bSave, bCancel, bEmail;
	EditText tEmail;
	Upload uploadPic;
	
	ImageView iv;
	Intent i;
	final static int cameraData = 0;
	private static final String TAG = "log_tag";
	Bitmap bmp;
	String filename, path;
	File file = null;
	ProgressDialog pd = null;
	Uri uriPath;
	
	private static final int ABOUT = 0;
	private static final int SETTINGS = 1;

	private static final int PICTURDS_MENU_ITEM = Menu.FIRST;
	private static final int CREATOR_MENU_ITEM = PICTURDS_MENU_ITEM + 1;

	private static final int EMAIL_MENU_ITEM = CREATOR_MENU_ITEM + 1;
	private static final int OPTION2_MENU_ITEM = EMAIL_MENU_ITEM + 1;
	private static final int CAPTURE_PIC = 0;
 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getPreferences(0);
	    boolean status = settings.getBoolean("status", false);

	    if(status==true){
	    	setContentView(R.layout.preview);
			takePhoto();
			initializw();
			EmailSession.setEmail(settings.getString("email", "null"));
			EmailSession.setStatus(true);
	    }
	    else{
	    	setContentView(R.layout.setemail);
	    	initializz();
	    }
	}
	
	private void takePhoto() {
		
		i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		uriPath = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "file.jpg"));
		Log.d("Camerainfo", Environment.getExternalStorageDirectory().toString());
		//i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		i.putExtra("return-data", true);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriPath);
        startActivityForResult(i, 0);
		
		

	}
	
	


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK) {
			path = uriPath.toString();
			file = new File(path);
			String[] separated = path.split("/");
			filename = separated[separated.length-1];
			
			bmp = loadBitmap(path);
			iv.setImageBitmap(bmp);
			
			Log.d("image: ", path);
			
			
		}
		else 
			takePhoto();
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
	}*/
	
	
	public Bitmap loadBitmap(String url)
	{
	    Bitmap bm = null;
	    InputStream is = null;
	    BufferedInputStream bis = null;
	    try 
	    {
	        URLConnection conn = new URL(url).openConnection();
	        conn.connect();
	        is = conn.getInputStream();
	        bis = new BufferedInputStream(is, 8192);
	        bm = BitmapFactory.decodeStream(bis);
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	    finally {
	        if (bis != null) 
	        {
	            try 
	            {
	                bis.close();
	            }
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	        if (is != null) 
	        {
	            try 
	            {
	                is.close();
	            }
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return bm;
	}


	private void initializw() {
		// TODO Auto-generated method stub
		iv = (ImageView) findViewById (R.id.iva);
		bUpload = (Button) findViewById (R.id.btnUpload);
		bSave = (Button) findViewById (R.id.btnSave);
		bCancel = (Button) findViewById (R.id.btnCancel);
		bUpload.setOnClickListener(this);
		bSave.setOnClickListener(this);
		bCancel.setOnClickListener(this);
	}

	private void initializz() {
		// TODO Auto-generated method stub
		bEmail = (Button) findViewById (R.id.btnEmail);
		tEmail = (EditText) findViewById (R.id.txtEmail);
		bEmail.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnUpload:
				//Upload uploadPic = new Upload();
				try {
					updateEmail();
					
					uploadPic = new Upload();
					
					uploadPic.setFilename(path.substring(7));
					uploadPic.setEmail(EmailSession.getEmail());
					uploadPic.setActivity(Camera.this);
					uploadPic.setObject(this);
					uploadPic.execute();
	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.btnSave:
				
				break;
			case R.id.btnCancel:
				takePhoto();
				break;
			case R.id.btnEmail:
				SharedPreferences settings = getPreferences(0);
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("status", true);
			    editor.putString("email", tEmail.getText().toString());
			    
			    // Commit the edits!
			    editor.commit();
			    
			    EmailSession.setEmail(tEmail.getText().toString());
			    EmailSession.setStatus(true);
			    
			    setContentView(R.layout.preview);
			    takePhoto();
			    initializw();
				break;
		}
	}
	
	private void updateEmail(){
		SharedPreferences settings = getPreferences(0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("status", true);
	    editor.putString("email", EmailSession.getEmail());
	    
	    // Commit the edits!
	    editor.commit();
	}

	
	
	private String getLastImagePath(){
	    final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
	    final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
	    Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
	    if(imageCursor.moveToFirst()){
	        int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
	        String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
	        Log.d(TAG, "getLastImageId::id " + id);
	        Log.d(TAG, "getLastImageId::fullpath " + fullPath);
	        imageCursor.close();
	        
	        return fullPath;
	    }
	    else return null;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu fileMenu = menu.addSubMenu("About");
        SubMenu editMenu = menu.addSubMenu("Settings");
        fileMenu.add(ABOUT, PICTURDS_MENU_ITEM, 0, "About Picturds");
        fileMenu.add(ABOUT, CREATOR_MENU_ITEM, 1, "Creators");
        editMenu.add(SETTINGS, EMAIL_MENU_ITEM, 0, "Change Email");
        editMenu.add(SETTINGS, OPTION2_MENU_ITEM, 1, "Force Exit");
        return super.onCreateOptionsMenu(menu);
    }
	
	//Skapar submenyn som anropas när man trycker på menu knappen på telefonen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case PICTURDS_MENU_ITEM:
        	About aboutDialog = new About(this);
        	aboutDialog.show();
            break;
        case CREATOR_MENU_ITEM:
        	Author authorDialog = new Author(this);
        	authorDialog.show();
            break;
        case EMAIL_MENU_ITEM:
        	EditEmail editDialog = new EditEmail(this,EmailSession.getEmail());
        	editDialog.show();
            break;
        case OPTION2_MENU_ITEM:
        	int pid = android.os.Process.myPid();
        	android.os.Process.killProcess(pid); 
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void showMsg(String message) {
        Toast msg = Toast.makeText(Camera.this, message, Toast.LENGTH_LONG);
        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
                msg.getYOffset() / 2);
        msg.show();
    }
    
    public void responseFromServer(String url) {
    	Looper.prepare();
    	Response response = new Response(this, url);
    	response.show();
    }
}
