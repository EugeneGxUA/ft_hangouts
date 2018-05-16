package com.example.d2_eugene.ft_hangouts;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThisApp {

	private final static String USER_ID = "userId";

	public static void saveProfile(Context context, JSONObject profile) throws JSONException, IOException  {

		final String fileName = profile.getString(USER_ID);

//		File folder = context.getFilesDir();
//		File[] profiles = folder.listFiles();
//		for (File user : profiles) {
//			if (user.getName().equals(fileName)) {
//				Toast.makeText(context, "This user is already exist", Toast.LENGTH_LONG).show();
//				return;
//			}
//		}

		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(profile.toString().getBytes());
		fos.flush();
		fos.close();

	}

}
