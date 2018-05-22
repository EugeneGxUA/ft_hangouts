package com.example.d2_eugene.ft_hangouts.ui.item;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.ui.activity.ChatActivity;
import com.example.d2_eugene.ft_hangouts.view.ViewCreatorWithArgument;

public class UserProfileShortView implements ViewCreatorWithArgument<Activity> {

	private final String firstName;
	private final String lastName;

	@Override
	public View onCreate(LayoutInflater inflater, ViewGroup container, final Activity activity) {
		final View rootView = inflater.inflate(R.layout.item_user_short, container, false);

		final ViewGroup userButton = rootView.findViewById(R.id.user_button); {
			userButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				ChatActivity.start(activity, firstName, lastName);
			} });
		}

		final TextView firstNameTextView = rootView.findViewById(R.id.user_name); {
			firstNameTextView.setText(firstName);
		}

		final TextView lastNameTextView = rootView.findViewById(R.id.user_lastName); {
			lastNameTextView.setText(lastName);
		}

		final ImageView userAvatar = rootView.findViewById(R.id.user_avatar); {

		}

		return rootView;
	}


	public UserProfileShortView(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
