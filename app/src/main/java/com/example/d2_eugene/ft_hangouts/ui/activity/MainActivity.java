package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.ui.fragment.ProfileFragment;
import com.example.d2_eugene.ft_hangouts.util.FragmentApp;

public class MainActivity extends Activity {

	private final FragmentManager fragmentManager = getFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final View settingsButton = findViewById(R.id.settings_button); {
			settingsButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				//TODO -> settings
			} });
		}

		final ViewGroup titleBar = findViewById(R.id.title_bar); {
			//TODO -> set color
		}

		final ViewGroup contentContainer = findViewById(R.id.content_container); {

		}

		final View addButton = findViewById(R.id.add_button); {
			addButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				final FragmentTransaction transaction = fragmentManager.beginTransaction();

				transaction.addToBackStack(null);
				transaction.replace(R.id.fragment_container, new ProfileFragment());
				transaction.commit();

			} });
		}

	}
}
