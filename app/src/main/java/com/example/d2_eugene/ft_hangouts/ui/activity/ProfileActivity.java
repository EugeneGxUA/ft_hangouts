package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.ThisApp;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.util.ValueChangedListener;
import com.example.d2_eugene.ft_hangouts.view.FloatingLabelField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		final ImageView profilePhotoView = findViewById(R.id.profile_image); {

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

		final TextView saveButton = findViewById(R.id.save_button); {
			saveButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				try {
					final Profile profile = new Profile(
						fistNameField.getValue(),
						lastNameField.getValue(),
						phoneNumberField.getValue(),
						emailField.getValue(),
						companyNameField.getValue(),
						ProfileActivity.this
					);

					JSONObject profileObject = new JSONObject();
					profileObject.put("userId", profile.getId());
					profileObject.put("firstName", profile.firstName);
					profileObject.put("lastName", profile.lastName);
					profileObject.put("phoneNumber", profile.phone);
					profileObject.put("companyName", profile.companyName);
					profileObject.put("email", profile.email);

					ThisApp.saveProfile(ProfileActivity.this, profileObject);
					finish();
				} catch (JSONException | IOException e) {
					throw new RuntimeException(e);
				}


			} });
		}


	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, ProfileActivity.class);

		//TODO -> some extra in INTENT

		context.startActivity(intent);

	}
}
