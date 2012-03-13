package com.picturds.picturds;

import java.io.InputStream;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Camera extends Activity implements View.OnClickListener{
	Button bUpload, bSave, bCancel, bEmail;
	EditText tEmail;
	
	ImageView iv;
	Intent i;
	final static int cameraData = 0;
	private static final String TAG = "log_tag";
	Bitmap bmp;
	String filename, path;
	File file = null;
	ProgressDialog pd = null;
	
	private static final int ABOUT = 0;
	private static final int SETTINGS = 1;

	private static final int PICTURDS_MENU_ITEM = Menu.FIRST;
	private static final int CREATOR_MENU_ITEM = PICTURDS_MENU_ITEM + 1;

	private static final int EMAIL_MENU_ITEM = CREATOR_MENU_ITEM + 1;
	private static final int OPTION2_MENU_ITEM = EMAIL_MENU_ITEM + 1;
	
	
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
		startActivityForResult(i, cameraData);
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
					
					Upload uploadPic = new Upload();
					
					uploadPic.setFilename(path);
					uploadPic.setEmail(EmailSession.getEmail());
					uploadPic.setActivity(Camera.this);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			path = getLastImagePath();
			file = new File(path);
			String[] separated = path.split("/");
			filename = separated[separated.length-1];
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
			
			
		}
		else 
			takePhoto();
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
}
