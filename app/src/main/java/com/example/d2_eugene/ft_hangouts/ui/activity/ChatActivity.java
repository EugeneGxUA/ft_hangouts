package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.anotation.NotNull;

public class ChatActivity extends Activity {

	private String firstName;
	private String lastName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		final Intent intent = getIntent();
		firstName = intent.getStringExtra("firstName");
		lastName = intent.getStringExtra("lastName");

		final ViewGroup profileButton = findViewById(R.id.user_profile_button); {


		}

		final ImageView userAvatar = findViewById(R.id.user_avatar); {

		}

		final TextView userFullName = findViewById(R.id.user_full_name); {
			final String fullName = firstName + " " + lastName;
			userFullName.setText(fullName);
		}

		final ImageView editButton = findViewById(R.id.edit_button); {

		}

		final ViewGroup messageContainer = findViewById(R.id.content_container); {

		}

		final EditText messageField = findViewById(R.id.message_field); {

		}

		final ImageView sendButton = findViewById(R.id.send_button); {

		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public static void start(Context context, @NotNull String firstName, @NotNull String lastName) {
		Intent intent = new Intent(context, ChatActivity.class);

		intent.putExtra("firstName", firstName);
		intent.putExtra("lastName", lastName);

		context.startActivity(intent);
	}
}
