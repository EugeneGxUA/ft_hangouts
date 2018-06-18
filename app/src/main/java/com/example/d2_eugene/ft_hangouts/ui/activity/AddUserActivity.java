package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.io.FileNotFoundException;
import java.io.IOException;

public class AddUserActivity extends Activity {

	@Nullable private Profile profile;
	private ImageView avatarImageView;
	private String imagePath;

	private static final String TAG = "AddUserActivity";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri imageUri = data.getData();
			avatarImageView.setImageURI(imageUri);
			imagePath = Profile.getRealPathFromURI(imageUri, AddUserActivity.this);
		} else {
			Toast.makeText(AddUserActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences themePrefs = getSharedPreferences("theme", Context.MODE_PRIVATE); {
			if (themePrefs.getString("theme", "").equals("light")) setTheme(R.style.Custom_light);
			else setTheme(R.style.Custom_dark);
		}

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

		avatarImageView = findViewById(R.id.profile_image); {
			if (profile != null) {
				try {
					avatarImageView.setImageBitmap(profile.getAvatarBitmap(this));
				} catch (FileNotFoundException e) {
					Toast.makeText(AddUserActivity.this, "NO PHOTO", Toast.LENGTH_SHORT).show();
				}
			}


			avatarImageView.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			} });
		}

		final FloatingLabelField fistNameField = findViewById(R.id.first_name); {
			fistNameField.setHintText(getString(R.string.first_name));
			fistNameField.setInputType(InputType.TYPE_CLASS_TEXT);
		}

		final FloatingLabelField lastNameField = findViewById(R.id.last_name); {
			lastNameField.setHintText(getString(R.string.last_name));
			lastNameField.setInputType(InputType.TYPE_CLASS_TEXT);
		}

		final FloatingLabelField phoneNumberField = findViewById(R.id.phone_number); {
			phoneNumberField.setHintText(getString(R.string.phone_number));
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
			emailField.setHintText(getString(R.string.e_mail));
			emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		}

		final FloatingLabelField companyNameField = findViewById(R.id.company_name); {
			companyNameField.setHintText(getString(R.string.company_name));
		}

		final ImageView deleteButton = findViewById(R.id.delete_button); {
			if (profile != null) {
				deleteButton.setVisibility(View.VISIBLE);
			} else {
				deleteButton.setVisibility(View.INVISIBLE);
			}
			deleteButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				if (profile != null) {
					profile.deleteProfile(AddUserActivity.this);
					MainActivity.start(AddUserActivity.this);
				}
			} });
		}

		if (profile != null) {
			fistNameField.setText(profile.firstName);
			lastNameField.setText(profile.lastName);
			phoneNumberField.setText(profile.phone);
			emailField.setText(profile.email);
			companyNameField.setText(profile.companyName);


			saveButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				try {

					String firstName = fistNameField.getValue();
					String lastName = lastNameField.getValue();
					String phone = phoneNumberField.getValue();
					if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
						Toast.makeText(AddUserActivity.this, getString(R.string.required_fields), Toast.LENGTH_SHORT).show();
						return;
					}
					if (phone.length() < 9 || phone.length() > 13) {
						Toast.makeText(AddUserActivity.this, getString(R.string.phone_validation_error), Toast.LENGTH_SHORT).show();
						return;
					}


					profile.firstName = firstName;
					profile.lastName = lastName;
					profile.phone = phone;
					profile.email = emailField.getValue();
					profile.companyName = companyNameField.getValue();

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
						View view = getCurrentFocus();

						String firstName = fistNameField.getValue();
						String lastName = lastNameField.getValue();
						String phone = phoneNumberField.getValue();
						if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
							Toast.makeText(AddUserActivity.this, getString(R.string.required_fields), Toast.LENGTH_SHORT).show();

							if (view != null) {
								InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
							}
							return;
						}
						if (phone.length() < 9 || phone.length() > 13) {
							Toast.makeText(AddUserActivity.this, getString(R.string.phone_validation_error), Toast.LENGTH_SHORT).show();
							if (view != null) {
								InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
							}
							return;
						}

						final Profile profile = new Profile(
							firstName,
							lastName,
							phone,
							emailField.getValue(),
							companyNameField.getValue(),
							AddUserActivity.this
						);

						Profile.saveProfile(AddUserActivity.this, profile.toJson(), imagePath);
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

		if (profile != null) {
			intent.putExtra("profile", profile.toJson().toString());
		}
		context.startActivity(intent);

	}
}
