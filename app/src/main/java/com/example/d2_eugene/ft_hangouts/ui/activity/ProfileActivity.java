package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.util.ValueChangedListener;
import com.example.d2_eugene.ft_hangouts.view.FloatingLabelField;

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

		final ImageView applyButton = findViewById(R.id.apply_button); {

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
