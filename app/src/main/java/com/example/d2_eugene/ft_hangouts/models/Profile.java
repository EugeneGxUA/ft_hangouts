package com.example.d2_eugene.ft_hangouts.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public class Profile {

	private static final String APP_PREFERENCES_USER_PROFILE = "userProfile";
	private static final String APP_PREFERENCES_USER_ID = "userId";


	private static final String DIR_PROFILES = "profiles";
	private static final String DIR_IMAGES = "images";

	private final int id;

	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;
//	public String avatarImage;

	@SuppressLint("ApplySharedPref")
	public Profile(String firstName, String lastName, String phone, @Nullable String email, @Nullable String companyName,/* @Nullable String avatarImage,*/ Context context) {

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
//		if (avatarImage != null) this.avatarImage = avatarImage;

	}

	public int getId() {
		return this.id;
	}

	public Profile(JSONObject user) {

		try {

			this.id = user.getInt(APP_PREFERENCES_USER_ID);
			this.firstName = user.getString("firstName");
			this.lastName = user.getString("lastName");
			this.phone = user.getString("phoneNumber");
			this.email = user.getString("email");
			this.companyName = user.getString("companyName");

//			if (!user.isNull("avatar")) this.avatarImage = user.optString("avatar");
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

//			if (avatarImage != null) user.put("avatar", avatarImage);

			return user;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveProfile(Context context, JSONObject profileJson, @Nullable String imagePath) throws JSONException, IOException {

		final String fileName = profileJson.getString(APP_PREFERENCES_USER_ID);

		File mainFolder = context.getFilesDir();

		File profilesDir = new File(mainFolder, DIR_PROFILES);
		if (!profilesDir.exists()) profilesDir.mkdir();

		if (imagePath != null) {

			File imageDir = new File(mainFolder, DIR_IMAGES);
			if (!imageDir.exists()) imageDir.mkdir();

			File profileImage = new File(imageDir, fileName + ".png");

			File imageFile = new File(imagePath);

			FileInputStream fileInputStream = new FileInputStream(imageFile);
			FileOutputStream fileOutputStream = new FileOutputStream(profileImage);

			while (true) {
				int element = fileInputStream.read();
				if (element == -1) break;

				fileOutputStream.write((byte) element);
			}
			fileInputStream.close();
			fileOutputStream.flush();
			fileOutputStream.close();
		}


		final File userProfile = new File(profilesDir, fileName);
		FileOutputStream profileFos = new FileOutputStream(userProfile);
		profileFos.write(profileJson.toString().getBytes());
		profileFos.flush();
		profileFos.close();

	}

	public void deleteProfile(Context context) {

		final File folder = context.getFilesDir();
		final File profileFolder = new File(folder, DIR_PROFILES);
		final File imageFolder = new File(folder, DIR_IMAGES);
		File file = new File(profileFolder, String.valueOf(this.id));
		File imageFile = new File(imageFolder, String.valueOf(this.id) + ".png");

		if (file.delete()) {
			imageFile.delete();
			Toast.makeText(context, "Profile was deleted", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Something went wrong please try later", Toast.LENGTH_SHORT).show();
		}

	}


	public static Profile[] readProfiles(Context context) throws JSONException, IOException {
		File folder = context.getFilesDir();
		File profileFolder = new File(folder, DIR_PROFILES);
		File[] files = profileFolder.listFiles();


		BufferedReader reader;

		if (files == null || files.length == 0) return new Profile[]{};

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

	public static Profile readProfileById(Context context, int id) throws JSONException, IOException {

		final File folder = context.getFilesDir();
		final File profileFolder = new File(folder, DIR_PROFILES);
		final File userFile = new File(profileFolder, String.valueOf(id));

		BufferedReader reader = new BufferedReader(new FileReader(userFile));
		StringBuilder stringBuilder = new StringBuilder();
		while (true) {
			int c = reader.read();
			if (c == -1) break;

			stringBuilder.append((char) c);
		}

		final JSONObject userJson = new JSONObject(stringBuilder.toString());
		return new Profile(userJson);
	}

	public static void editProfile(Context context, JSONObject newProfile) throws JSONException, IOException {
		final File folder = context.getFilesDir();
		final File profileFolder = new File(folder, DIR_PROFILES);
		final File file = new File(profileFolder, newProfile.getString(APP_PREFERENCES_USER_ID));

		if (!file.exists()) {
			// TODO: 23.05.18
			throw new RuntimeException();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(newProfile.toString());
		}
	}

	public static String getRealPathFromURI(Uri contentURI, Activity context) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = context.managedQuery(contentURI, projection, null,
			null, null);
		if (cursor == null)
			return null;
		int column_index = cursor
			.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		if (cursor.moveToFirst()) {
			String s = cursor.getString(column_index);
			 cursor.close();
			return s;
		}
		cursor.close();
		return null;
	}

	public Bitmap getAvatarBitmap(Context context) throws FileNotFoundException {

		final File mainFolder = context.getFilesDir();
		final File imagesFolder = new File(mainFolder, "images");
		final File profileImageName = new File(imagesFolder, String.valueOf(id + ".png"));

		return BitmapFactory.decodeStream(new FileInputStream(profileImageName));
	}
}
