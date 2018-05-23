package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.anotation.NotNull;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.ui.model.SmsMessage;
import com.example.d2_eugene.ft_hangouts.view.SmsMessageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class ChatActivity extends Activity {

	private Profile profile;
	private ViewGroup messageContainer;

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == 1) {
			for (int i = 0; i < permissions.length; i++) {
				String permission = permissions[i];
				int requestResult = grantResults[i];

				if (permission.equals(Manifest.permission.READ_SMS))  {
					if (requestResult == PackageManager.PERMISSION_GRANTED) {
						fillContainer();
					} else {
						onBackPressed();
					}
				} else if (permission.equals(Manifest.permission.SEND_SMS)) {
					if (requestResult == PackageManager.PERMISSION_GRANTED) {
						//TODO -> make send sms method()
					} else {
						Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		final Intent intent = getIntent();
		try {
			profile = new Profile(new JSONObject(intent.getStringExtra("profile")));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		final ViewGroup profileButton = findViewById(R.id.user_profile_button); {


		}

		final ImageView userAvatar = findViewById(R.id.user_avatar); {

		}

		final TextView userFullName = findViewById(R.id.user_full_name); {
			final String fullName = profile.firstName + " " + profile.lastName;
			userFullName.setText(fullName);
		}

		final ImageView editButton = findViewById(R.id.edit_button); {
			editButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				//todo - return new profile if edit
				AddUserActivity.start(ChatActivity.this, profile);
			} });
		}

		messageContainer = findViewById(R.id.content_container); {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 1);
				} else {
					fillContainer();
				}
			} else {
				fillContainer();
			}
		}

		final EditText messageField = findViewById(R.id.message_field); {

		}

		final ImageView sendButton = findViewById(R.id.send_button); {
			final String msg = messageField.getText().toString();


		}
	}

	private ArrayList<SmsMessage> fetchSmsInbox() {
		ArrayList<SmsMessage> smsMessages = new ArrayList<SmsMessage>();

		Uri smsInboxUri = Uri.parse("content://sms/");

		try ( Cursor cursor = getContentResolver().query(smsInboxUri, new String[]{"_id", "address", "date", "body", "type"}, null, null, null) ){
			if (cursor == null) throw new RuntimeException();

			if (cursor.moveToFirst()) {

				int counter = cursor.getCount();
				for (int i = 0; i < counter; i++) {

					String type = cursor.getString(4);
					String address = cursor.getString(1);

					if (address.equals(profile.phone)) {
						String date = cursor.getString(2);
						String body = cursor.getString(3);

						final String from; {
							if (Integer.parseInt(type) == Telephony.Sms.MESSAGE_TYPE_SENT) {
								from = "Me :";
							} else {
								from = "He :";
							}
						}
						smsMessages.add(new SmsMessage(from, body, date));
					}

					cursor.moveToNext();

				}
			}
		} catch (Throwable e) {
			Log.e("CHECK_SMS", "fetchSmsInbox: ", e);
		}
		return smsMessages;
	}

	private void fillContainer() {
		ArrayList<SmsMessage> smsMessages = fetchSmsInbox();
		LayoutInflater inflater = getLayoutInflater();

		messageContainer.removeAllViews();
		for (int i = 0; i < smsMessages.size(); i++) {
			messageContainer.addView(new SmsMessageView(smsMessages.get(i).from, smsMessages.get(i).body).onCreate(inflater, messageContainer, ChatActivity.this));
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public static void start(Context context, @NotNull Profile profile) {
		Intent intent = new Intent(context, ChatActivity.class);

		intent.putExtra("profile", profile.toJson().toString());



		context.startActivity(intent);
	}
}
