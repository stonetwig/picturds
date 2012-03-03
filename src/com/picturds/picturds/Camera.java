package com.picturds.picturds;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

public class Camera extends Activity implements View.OnClickListener{
	Button bUpload, bSave, bCancel;
	ImageView iv;
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		initializw();
		InputStream is = null;
		bmp = BitmapFactory.decodeStream(is);
		takePhoto();
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


	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnUpload:
				Upload uploadPic = new Upload();
			try {
				uploadPic.executeMultipartPost(bmp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			case R.id.btnSave:
				
				break;
			case R.id.btnCancel:
				
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
		}
		else 
			takePhoto();
	}
}
