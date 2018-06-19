package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.example.d2_eugene.ft_hangouts.annotation.NotNull;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.models.SmsMessage;
import com.example.d2_eugene.ft_hangouts.ui.view.SmsMessageView;
import com.example.d2_eugene.ft_hangouts.util.SmsBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class ChatActivity extends Activity {

	private Profile profile;
	private int id;
	private ViewGroup messageContainer;

	private TextView userFullName;
	private View titleBar;
	private View bottomTextView;
	private EditText messageField;

	boolean darkTheme = true;

	private SmsBroadcastReceiver smsBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences themePrefs = getSharedPreferences("theme", Context.MODE_PRIVATE);
		{
			if (themePrefs.getString("theme", "").equals("light")) {
				setTheme(R.style.Custom_light);
				darkTheme = false;
			} else setTheme(R.style.Custom_dark);
		}

		setContentView(R.layout.activity_chat);

		{
			final Intent intent = getIntent();
			id = intent.getIntExtra("userId", 0);
		}

		final ViewGroup profileButton = findViewById(R.id.user_profile_button);

		final ImageView userAvatar = findViewById(R.id.user_avatar);

		userFullName = findViewById(R.id.user_full_name);

		final ImageView editButton = findViewById(R.id.edit_button);
		{
			editButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AddUserActivity.start(ChatActivity.this, profile);
				}
			});
		}

		final ImageView callButton = findViewById(R.id.call_button);
		{
			callButton.setOnClickListener(new View.OnClickListener() { @TargetApi(Build.VERSION_CODES.M)
			@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + profile.phone));
					if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						return;
					}
					ChatActivity.this.startActivity(intent);
			} });
		}

		messageContainer = findViewById(R.id.content_container);
		messageField = findViewById(R.id.message_field);

		final ImageView sendButton = findViewById(R.id.send_button);

		titleBar = findViewById(R.id.title_bar);

		bottomTextView = findViewById(R.id.bottom_text_layout);
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			profile = Profile.readProfileById(ChatActivity.this, id);
		} catch (JSONException | IOException e) {
			throw new RuntimeException(e);
		}

		final ImageView userAvatar = findViewById(R.id.user_avatar); {
			try {
				userAvatar.setImageBitmap(profile.getAvatarBitmap(this));
			} catch (FileNotFoundException e) {
				Toast.makeText(ChatActivity.this, "NO PHOTO", Toast.LENGTH_SHORT).show();
			}
		}

		{
			final String fullName = profile.firstName + " " + profile.lastName;
			userFullName.setText(fullName);
		}

		final ImageView editButton = findViewById(R.id.edit_button); {
			editButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				AddUserActivity.start(ChatActivity.this, profile);
			} });
		}

		messageContainer = findViewById(R.id.content_container); {
			fillContainer();
		}




		final ImageView sendButton = findViewById(R.id.send_button); {
			sendButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				final String msg = messageField.getText().toString();
				if (msg.isEmpty()) return;

				try {
					SmsManager.getDefault().sendTextMessage(profile.phone, null, msg, null, null);
					messageField.setText("");
					fillContainer();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ChatActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
				}
			} });
		}

		if (darkTheme) {
			titleBar.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
			bottomTextView.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
		}

		smsBroadcastReceiver = new SmsBroadcastReceiver(profile.phone);
		registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
		smsBroadcastReceiver.listener = new SmsBroadcastReceiver.SmsListener() {
			@Override
			public void onTextReceived(String text) {
				fillContainer();
			}
		};

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsBroadcastReceiver);
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
							if (Integer.parseInt(type) == Telephony.Sms.MESSAGE_TYPE_SENT || Integer.parseInt(type) == Telephony.Sms.MESSAGE_TYPE_OUTBOX) {
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

	public static void start(Context context, int id) {
		Intent intent = new Intent(context, ChatActivity.class);

		intent.putExtra("userId", id);

		context.startActivity(intent);
	}
}
