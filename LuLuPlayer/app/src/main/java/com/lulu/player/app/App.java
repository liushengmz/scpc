package com.lulu.player.app;

import android.app.Application;
import android.content.Context;

public class App extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
	}

	public static Context getContext() {
		return context;
	}

	public static void exit() {
		System.exit(0);
	}

}
