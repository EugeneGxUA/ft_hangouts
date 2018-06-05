package com.example.d2_eugene.ft_hangouts.ui.view;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.ui.activity.ChatActivity;
import com.example.d2_eugene.ft_hangouts.util.ViewCreatorWithArgument;

import org.json.JSONException;

import java.io.IOException;

public class UserProfileShortView implements ViewCreatorWithArgument<Activity> {

	private final int id;

	@Override
	public View onCreate(LayoutInflater inflater, ViewGroup container, final Activity activity) {
		final View rootView = inflater.inflate(R.layout.item_user_short, container, false);

		final Profile profile; {
			try {
				profile = Profile.readProfileById(activity, id);
			} catch (JSONException | IOException e) {
				throw new RuntimeException(e);
			}
		}


		final ViewGroup userButton = rootView.findViewById(R.id.user_button); {
			userButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
				ChatActivity.start(activity, id);
			} });
		}

		final TextView firstNameTextView = rootView.findViewById(R.id.user_name); {
			firstNameTextView.setText(profile.firstName);
		}

		final TextView lastNameTextView = rootView.findViewById(R.id.user_lastName); {
			lastNameTextView.setText(profile.lastName);
		}

		final ImageView userAvatar = rootView.findViewById(R.id.user_avatar); {
			if (profile.avatarImage != null) {
				final Uri imageUri = Uri.parse(profile.avatarImage);
				if (imageUri != null) {
					userAvatar.setImageURI(imageUri);
				}
			}
		}

		return rootView;
	}


	public UserProfileShortView(int id) {
		this.id = id;
	}

}
