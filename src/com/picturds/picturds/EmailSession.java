package com.picturds.picturds;

import android.app.Application;

public class EmailSession extends Application {

	private static String email;
	private static Boolean status;
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    email="";
	    status=false;
	}
	
	public static String getEmail() {
	    return email;
	}
	
	public static void setEmail(String mail) {
	    EmailSession.email = mail;
	}
	
	public static Boolean getStatus() {
	    return status;
	}
	
	public static void setStatus(Boolean status) {
	    EmailSession.status = status;
	}
	
	public static void logout() {
	    email="";
	    status=false;
	}
}
