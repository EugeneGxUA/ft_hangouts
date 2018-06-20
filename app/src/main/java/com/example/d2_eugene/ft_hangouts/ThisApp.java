package com.example.d2_eugene.ft_hangouts;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.util.SmsBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ThisApp extends Application{



	public void onActivityPaused() {
		final SharedPreferences sharedPreferences = getSharedPreferences("lastPausedTime", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong("lastTime", System.currentTimeMillis());
		editor.commit();
	}

	public long getLastActivityTime() {
		final SharedPreferences sharedPreferences = getSharedPreferences("lastPausedTime", Context.MODE_PRIVATE);
		return sharedPreferences.getLong("lastTime", 0);
	}

	public static ThisApp get(Context context) {
		return (ThisApp) context.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
