package com.example.d2_eugene.ft_hangouts;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class ThisApp {

	public static void saveProfile(Context context, JSONObject profile) throws JSONException, IOException  {

		final String fileName = profile.getString("Id");
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(profile.toString().getBytes());
		fos.flush();
		fos.close();

	}

}
