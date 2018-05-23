package com.example.d2_eugene.ft_hangouts;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
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

	private SmsBroadcastReceiver smsBroadcastReceiver;

	@Override
	public void onCreate() {
		super.onCreate();
		smsBroadcastReceiver = new SmsBroadcastReceiver("", "");
		registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
	}

	@Override
	public void onTerminate() {
		unregisterReceiver(smsBroadcastReceiver);
		super.onTerminate();
	}

	private final static String USER_ID = "userId";

	public static void saveProfile(Context context, JSONObject profile) throws JSONException, IOException  {

		final String fileName = profile.getString(USER_ID);

		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(profile.toString().getBytes());
		fos.flush();
		fos.close();

	}

	public static Profile[] readProfiles(Context context) throws JSONException, IOException {
		File folder = context.getFilesDir();
		File[] files = folder.listFiles();

		BufferedReader reader;

		Profile[] users = new Profile[files.length];
		for (int i = 0; i < files.length; i++) {
			File profile = files[i];
			reader = new BufferedReader(new FileReader(profile));
			StringBuilder stringBuilder = new StringBuilder();
			while (true) {
				int c = reader.read();
				if (c == -1) break;

				stringBuilder.append((char) c);
			}
			JSONObject user = new JSONObject(stringBuilder.toString());
			users[i] = new Profile(user);

		}

		return users;
	}

	public static void editProfile(Context context, JSONObject newProfile) throws JSONException, IOException {
		final File folder = context.getFilesDir();
		final File file = new File(folder, newProfile.getString("userId"));

		if (!file.exists()) {
			// TODO: 23.05.18
			throw new RuntimeException();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(newProfile.toString());

			Log.d("CHECK_FILE", "editProfile: " + file.getName());
			Log.d("CHECK_FILE", "editProfile: " + newProfile.toString());
		}
	}



}
