package com.picturds.picturds;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditEmail extends Dialog implements OnClickListener {
	
	EditText tEmail;
	Button bSubmit;
	String email;
	Context parent;
	
	public EditEmail(Context context, String email) {
		super(context);
		parent = context;
		
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.setemail);
		this.setCancelable(true);
		
		bSubmit = (Button) findViewById(R.id.btnEmail);
		tEmail = (EditText) findViewById(R.id.txtEmail);
		
		bSubmit.setOnClickListener(this);
		
        tEmail.setText(email);
	}
	
	public void onClick(View v) {
		//Körs om man trycker på edit knappen
		if (v == bSubmit){
			SharedPreferences settings = ((Activity) parent).getPreferences(0);
			SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean("status", true);
		    editor.putString("email", tEmail.getText().toString());
		    
		    // Commit the edits!
		    editor.commit();
		    
		    EmailSession.setEmail(tEmail.getText().toString());
		    EmailSession.setStatus(true);
		    
		    dismiss();
		}
	}
}
