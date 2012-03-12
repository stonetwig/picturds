/*
 * AUTHORS:
 * Andreas Thuresson
 * Sofie Csaba
 */

package com.picturds.picturds;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Dialog implements OnClickListener {
	
	Button bBack;
	
	//Skapar dialog med About xml filen
	public About(Context context) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.about);
        
        bBack = (Button)findViewById(R.id.btnAboutBack);
        bBack.setOnClickListener(this);
    }

	public void onClick(View v) {
		if (v == bBack)
			dismiss();
	}
}