package com.example.d2_eugene.ft_hangouts.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.util.FragmentApp;

public class ProfileFragment extends FragmentApp {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

		final Activity activity = getParentActivity();

		final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


		return rootView;
	}
}
