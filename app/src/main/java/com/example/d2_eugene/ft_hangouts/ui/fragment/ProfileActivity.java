package com.example.d2_eugene.ft_hangouts.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.d2_eugene.ft_hangouts.R;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		final ImageView profilePhotoView = findViewById(R.id.profile_image);

		final EditText fistNameField = findViewById(R.id.first_name);

		final EditText lastNameField = findViewById(R.id.last_name);

		final EditText phoneNumberField = findViewById(R.id.phone_number);

		final EditText emailField = findViewById(R.id.email);

		final EditText companyNameField = findViewById(R.id.company_name);
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, ProfileActivity.class);

		//TODO -> some extra in INTENT

		context.startActivity(intent);

	}
}
