package com.example.d2_eugene.ft_hangouts.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.d2_eugene.ft_hangouts.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Profile {

	private static final String APP_PREFERENCES_USER_PROFILE = "userProfile";
	private static final String APP_PREFERENCES_USER_ID = "userId";

	private final int id;

	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;
	public String avatarImage;

	@SuppressLint("ApplySharedPref")
	public Profile(String firstName, String lastName, String phone, @Nullable String email, @Nullable String companyName, @Nullable String avatarImage, Context context) {

		final SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES_USER_PROFILE, Context.MODE_PRIVATE);
		this.id = sharedPreferences.getInt(APP_PREFERENCES_USER_ID, 1);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(APP_PREFERENCES_USER_ID, this.id + 1);
		editor.commit();

		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.companyName = companyName;
		this.avatarImage = avatarImage;

	}

	public Profile(JSONObject user) {

		try {

			this.id = user.getInt(APP_PREFERENCES_USER_ID);
			this.firstName = user.getString("firstName");
			this.lastName = user.getString("lastName");
			this.phone = user.getString("phoneNumber");
			this.email = user.getString("email");
			this.companyName = user.getString("companyName");
			this.avatarImage = user.getString("avatar");
		}catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public JSONObject toJson() {
		JSONObject user = new JSONObject();

		try {
			user.put(APP_PREFERENCES_USER_ID, id);
			user.put("firstName", firstName);
			user.put("lastName", lastName);
			user.put("phoneNumber", phone);
			user.put("companyName", companyName);
			user.put("email", email);
			user.put("avatar", avatarImage);

			return user;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveProfile(Context context, JSONObject profile) throws JSONException, IOException {

		final String fileName = profile.getString(APP_PREFERENCES_USER_ID);

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
		final File file = new File(folder, newProfile.getString(APP_PREFERENCES_USER_ID));

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
