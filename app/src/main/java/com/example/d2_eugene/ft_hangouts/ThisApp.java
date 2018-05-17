package com.example.d2_eugene.ft_hangouts;

import android.content.Context;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.models.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ThisApp {

	private final static String USER_ID = "userId";

	public static void saveProfile(Context context, JSONObject profile) throws JSONException, IOException  {

		final String fileName = profile.getString(USER_ID);

		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(profile.toString().getBytes());
		fos.flush();
		fos.close();

	}

	public static Profile[] readProfiles(Context context) throws JSONException, IOException {
		JSONArray allUsers = new JSONArray();

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

}
