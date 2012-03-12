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

public class Author extends Dialog implements OnClickListener {
	
	Button bBack;
	
	//Skapar dialgofönster men author xml filen
	public Author(Context context) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.author);
        
        bBack = (Button)findViewById(R.id.btnAuthorBack);
        bBack.setOnClickListener(this);
    }

	public void onClick(View v) {
		if (v == bBack)
			dismiss();
	}
}