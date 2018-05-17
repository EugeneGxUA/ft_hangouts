package com.example.d2_eugene.ft_hangouts.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.d2_eugene.ft_hangouts.anotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {

	private static final String APP_PREFERENCES_USER_PROFILE = "userProfile";
	private static final String APP_PREFERENCES_USER_ID = "userId";

	private final int id;

	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;

	@SuppressLint("ApplySharedPref")
	public Profile(String firstName, String lastName, String phone, @Nullable String email, @Nullable String companyName, Context context) {

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

	}

	public Profile(JSONObject user) {

		try {
			this.id = user.getInt("userId");
			this.firstName = user.getString("firstName");
			this.lastName = user.getString("lastName");
			this.phone = user.getString("phoneNumber");
			this.email = user.getString("email");
			this.companyName = user.getString("companyName");
		}catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public JSONObject toJson() {
		JSONObject user = new JSONObject();

		try {
			user.put("userId", id);
			user.put("firstName", firstName);
			user.put("lastName", lastName);
			user.put("phoneNumber", phone);
			user.put("companyName", companyName);
			user.put("email", email);

			return user;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
