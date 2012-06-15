package com.picturds.picturds;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("deprecation")
public class Camera extends Activity implements View.OnClickListener{
	Button bUpload, bEmail;
	Item bSave, bCancel;
	EditText tEmail;
	Upload uploadPic;
	int width, height, storage = 0;
	ImageView iv;
	Intent i;
	final static int cameraData = 0;
	private static final String TAG = "log_tag";
	Bitmap bmp;
	String filename, path;
	File file = null;
	ProgressDialog pd = null;
	Uri uriPath;
	private String url = null;

	private static final int PICTURDS_MENU_ITEM = Menu.FIRST;
	private static final int CREATOR_MENU_ITEM = PICTURDS_MENU_ITEM + 1;

	private static final int EMAIL_MENU_ITEM = CREATOR_MENU_ITEM + 1;
	private static final int OPTION2_MENU_ITEM = EMAIL_MENU_ITEM + 1;
 
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getPreferences(0);
	    boolean status = settings.getBoolean("status", false);
	    
	    Display display = getWindowManager().getDefaultDisplay(); 
	    width = display.getWidth();  // deprecated
	    height = display.getHeight();  // deprecated

	    
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
	
	private void savePhoto() {
		if(uriPath != null) {
			uriPath = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url + ".jpg"));
			showMsg("Picture saved!");
		}
		else
			showMsg("You'll need to take a photo before you can save it!");
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK) {
			path = uriPath.toString();
			file = new File(path);
			String[] separated = path.split("/");
			filename = separated[separated.length-1];
			
			//Before setting image view, load it to its correct parameters..
			iv.getLayoutParams().height = height;
			iv.getLayoutParams().width = width;
			
			bmp = loadBitmap(path);
			iv.setImageBitmap(bmp);
			Log.d("image: ", path);
			
			//upload photo directly
			onClick(bUpload);
			
		}
		
		else
			finish();
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
		iv = (ImageView) findViewById (R.id.iva);
		bUpload = (Button) findViewById (R.id.btnUpload);
		bUpload.setOnClickListener(this);
		//TODO BEAM PHOTO
		//iv.setOnClickListener(this);
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
			case R.id.menu_save_photo:
				if(storage > 0) {
					ClipboardManager ClipMan = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
					if(url != null) {
						ClipMan.setText("http://picturds.com/i/" + url);
						showMsg("Copied URL to clipboard");
					}
					else {
						ClipMan.setText("Couldn't retrieve URL, sorry.");
						showMsg("Sorry, upload probably failed.. :(");
					}
					finish();
				}
				storage++;
				Response responseDialog = new Response(this, url);
	        	responseDialog.show();
				break;
			case R.id.menu_new_photo:
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

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
	
	//Skapar submenyn som anropas när man trycker på menu knappen på telefonen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about_picturds:
        	About aboutDialog = new About(this);
        	aboutDialog.show();
            break;
        case R.id.authors:
        	Author authorDialog = new Author(this);
        	authorDialog.show();
            break;
        case R.id.change_email:
        	EditEmail editDialog = new EditEmail(this,EmailSession.getEmail());
        	editDialog.show();
            break;
        case R.id.kill_app:
        	int pid = android.os.Process.myPid();
        	android.os.Process.killProcess(pid); 
            break;
        case R.id.menu_save_photo:
        	//savePhoto();
        	showMsg("This feature isn't implemented yet, sorry! :(");
        	break;
        case R.id.menu_new_photo:
        	takePhoto();
        	break;
        case R.id.menu_upload:
        	onClick(bUpload);
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
    	this.url = url;
    	Response response = new Response(this, this.url);
    	response.show();
    } 
}
