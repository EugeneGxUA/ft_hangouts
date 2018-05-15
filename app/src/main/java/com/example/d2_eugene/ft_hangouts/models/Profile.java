package com.example.d2_eugene.ft_hangouts.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.d2_eugene.ft_hangouts.anotation.Nullable;

public class Profile {

	private int id;
	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;

	public Profile(String firstName, String lastName, String phone, @Nullable String email, @Nullable String companyName, Context context) {


		final SharedPreferences preferences = context.getSharedPreferences("userId", Context.MODE_PRIVATE);
		if (preferences.contains("userId")) {
			this.id = preferences.getInt("userId", 1);
			this.id++;
		} else {
			this.id = 1;
		}

		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.companyName = companyName;

	}

	public int getId() {
		return this.id;
	}

}
