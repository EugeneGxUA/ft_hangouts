package com.example.d2_eugene.ft_hangouts.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.util.FragmentApp;

public class ProfileFragment extends FragmentApp {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

		final Activity activity = getParentActivity();

		final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

		final ImageView profilePhotoView = rootView.findViewById(R.id.profile_image);

		final EditText fistNameField = rootView.findViewById(R.id.first_name);

		final EditText lastNameField = rootView.findViewById(R.id.last_name);

		final EditText phoneNumberField = rootView.findViewById(R.id.phone_number);

		final EditText emailField = rootView.findViewById(R.id.email);

		final EditText companyNameField = rootView.findViewById(R.id.company_name);


		return rootView;
	}
}
