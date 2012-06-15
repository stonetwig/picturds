/*
 * AUTHORS:
 * Andreas Thuresson
 * Markus Stenqvist
 */

package com.picturds.picturds;

import android.app.Dialog;
import android.content.Context;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


@SuppressWarnings("deprecation")
public class Response extends Dialog implements OnClickListener {
	
	Button cpandclose;
	EditText response;
	Context context;
	
	//Skapar dialog med About xml filen
	public Response(Context context, String url) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setTitle("Upload Complete!");
		/** Design the dialog in main.xml file */
		setContentView(R.layout.response);
		response = (EditText) findViewById(R.id.resposeurl);
        cpandclose = (Button) findViewById(R.id.cpandclose);
        cpandclose.setOnClickListener(this);
		//response.setText("http://picturds.com/i/" + url);
        response.setText("http://picturds.com/i/" + url);
        this.context = context;
        Log.d("Response Window: ", "running..");
    }

	
	public void setText(String url) {
		response.setText("http://picturds.com/i/" + url);
		response.selectAll();
	}

	public void onClick(View v) {
		if (v == cpandclose) {
			ClipboardManager ClipMan = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipMan.setText(response.getText());
			int pid = android.os.Process.myPid();
        	android.os.Process.killProcess(pid); 
			dismiss();
		}
	}
	


}