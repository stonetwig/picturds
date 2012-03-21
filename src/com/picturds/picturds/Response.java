/*
 * AUTHORS:
 * Andreas Thuresson
 * Markus Stenqvist
 */

package com.picturds.picturds;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.text.ClipboardManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;

public class Response extends Dialog implements OnClickListener {
	
	Button cpandclose;
	EditText response;
	Context context;
	
	//Skapar dialog med About xml filen
	public Response(Context context, String url) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.response);
        
        cpandclose = (Button) findViewById(R.id.cpandclose);
        cpandclose.setOnClickListener(this);
        response = (EditText) findViewById(R.id.resposeurl);
        response.setText(url);
        this.context = context;
    }

	public void onClick(View v) {
		if (v == cpandclose) {
			ClipboardManager ClipMan = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipMan.setText(response.getText());
			dismiss();
		}
	}
}