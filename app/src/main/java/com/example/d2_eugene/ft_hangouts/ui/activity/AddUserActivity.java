package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.annotation.Nullable;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.util.ValueChangedListener;
import com.example.d2_eugene.ft_hangouts.ui.view.FloatingLabelField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddUserActivity extends Activity {

	@Nullable private Profile profile;
	private ImageView avatarImage;
	private Uri imagePath;
	//TODO -> make image save

	private static final String TAG = "AddUserActivity";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			imagePath = data.getData();
			avatarImage.setImageURI(imagePath);
		} else {
			Toast.makeText(AddUserActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		final TextView saveButton = findViewById(R.id.save_button);

		final String profileExtra = getIntent().getStringExtra("profile");
		if (profileExtra != null) {
			try {
				profile = new Profile(new JSONObject(profileExtra));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		} else {
			profile = null;
		}

		avatarImage = findViewById(R.id.profile_image); {
			avatarImage.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			} });
		}

		final FloatingLabelField fistNameField = findViewById(R.id.first_name); {
			fistNameField.setHintText("First name");
			fistNameField.setInputType(InputType.TYPE_CLASS_TEXT);
		}

		final FloatingLabelField lastNameField = findViewById(R.id.last_name); {
			lastNameField.setHintText("Last name");
			lastNameField.setInputType(InputType.TYPE_CLASS_TEXT);
		}

		final FloatingLabelField phoneNumberField = findViewById(R.id.phone_number); {
			phoneNumberField.setHintText("Phone number");
			phoneNumberField.setInputType(InputType.TYPE_CLASS_PHONE);

			phoneNumberField.addValueChangeListener(new ValueChangedListener() {
				@Override
				public void onValueChanged(String oldValue, String value) {
					if (value.length() > 13) phoneNumberField.setText(oldValue);
				}
			});
		}
		//TODO VALIDATION

		final FloatingLabelField emailField = findViewById(R.id.email); {
			emailField.setHintText("E-mail");
			emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		}

		final FloatingLabelField companyNameField = findViewById(R.id.company_name); {
			companyNameField.setHintText("Company");
		}

		if (profile != null) {
			fistNameField.setText(profile.firstName);
			lastNameField.setText(profile.lastName);
			phoneNumberField.setText(profile.phone);
			emailField.setText(profile.email);
			companyNameField.setText(profile.companyName);

			saveButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				try {

					profile.firstName = fistNameField.getValue();
					profile.lastName = lastNameField.getValue();
					profile.phone = phoneNumberField.getValue();
					profile.email = emailField.getValue();
					profile.companyName = companyNameField.getValue();
					profile.avatarImage = imagePath;

					Profile.editProfile(AddUserActivity.this, profile.toJson());
					finish();
				} catch (JSONException | IOException e) {
					throw new RuntimeException(e);
				}
			} });
		} else {
			saveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						final Profile profile = new Profile(
							fistNameField.getValue(),
							lastNameField.getValue(),
							phoneNumberField.getValue(),
							emailField.getValue(),
							companyNameField.getValue(),
							imagePath,
							AddUserActivity.this
						);

						Profile.saveProfile(AddUserActivity.this, profile.toJson());
						finish();
					} catch (JSONException | IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public static void start(Context context, @Nullable Profile profile) {
		Intent intent = new Intent(context, AddUserActivity.class);

		if (profile != null) intent.putExtra("profile", profile.toJson().toString());
		context.startActivity(intent);

	}
}
