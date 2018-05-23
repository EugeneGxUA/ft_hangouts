package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.ThisApp;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.ui.model.UserProfileShortView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends Activity {

	private ViewGroup contentContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contentContainer = findViewById(R.id.content_container);

		final View settingsButton = findViewById(R.id.settings_button); {
			settingsButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				//TODO -> settings
			} });
		}

		final ViewGroup titleBar = findViewById(R.id.title_bar); {
			//TODO -> set color
		}



		final View addButton = findViewById(R.id.add_button); {
			addButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				AddUserActivity.start(MainActivity.this, null);
			} });
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			Profile[] allUsers = ThisApp.readProfiles(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			contentContainer.removeAllViews();
			for (Profile profile : allUsers) {

				contentContainer.addView(new UserProfileShortView(profile).onCreate(inflater, contentContainer, MainActivity.this));

			}

		} catch (JSONException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
