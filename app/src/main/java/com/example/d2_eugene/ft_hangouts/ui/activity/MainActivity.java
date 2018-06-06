package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.ui.view.UserProfileShortView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends Activity {

	private ViewGroup contentContainer;

	private static final String TAG = "MainActivity";

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == 1) {
			for (int i = 0; i < permissions.length; i++) {
				String permission = permissions[i];
				int requestResult = grantResults[i];

				if (permission.equals(Manifest.permission.READ_SMS))  {
					if (requestResult == PackageManager.PERMISSION_GRANTED) {
						Toast.makeText(this, "Read sms granted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Need permission for sms read", Toast.LENGTH_SHORT).show();
					}
				} else if (permission.equals(Manifest.permission.SEND_SMS)) {
					if (requestResult == PackageManager.PERMISSION_GRANTED) {
						Toast.makeText(this, "Send sms granted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Need permission for sms send", Toast.LENGTH_SHORT).show();
					}
				} else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
					if (requestResult == PackageManager.PERMISSION_GRANTED) {
						Toast.makeText(this, "Read storage granted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Need permission for sms send", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contentContainer = findViewById(R.id.content_container);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if	(
					checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
					checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

				)
			{
				requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
			} else {
				finish();
			}
		}

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
			Profile[] allUsers = Profile.readProfiles(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			contentContainer.removeAllViews();
			for (Profile profile : allUsers) {

				contentContainer.addView(new UserProfileShortView(profile.getId()).onCreate(inflater, contentContainer, MainActivity.this));

			}

		} catch (JSONException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 Call this method()
	 when you need finish all activity
	 and open MainActivity
	 */
	public static void start(Context context) {
		final Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
