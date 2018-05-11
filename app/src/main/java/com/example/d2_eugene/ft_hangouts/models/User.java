package com.example.d2_eugene.ft_hangouts.models;

import com.example.d2_eugene.ft_hangouts.anotation.Nullable;

public class User {

	public String id;
	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String companyName;

	public User(String firstName, String lastName, String phone, @Nullable String email, @Nullable String companyName) {

		this.id = String.valueOf(System.currentTimeMillis());
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.companyName = companyName;

	}

}
