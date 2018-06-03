package com.example.d2_eugene.ft_hangouts.ui.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.util.ViewCreatorWithArgument;

public class SmsMessageView implements ViewCreatorWithArgument<Activity> {

	private final String smsSender;
	private final String smsBody;

	@Override
	public View onCreate(LayoutInflater inflater, ViewGroup container, Activity argument) {

		final View rootView = inflater.inflate(R.layout.view_sms_message, container, false);

		final ViewGroup messageItemView = rootView.findViewById(R.id.sms_message_view);


		final TextView senderNumber = rootView.findViewById(R.id.sender_number); {
			senderNumber.setText(smsSender);
		}

		final TextView messageBody = rootView.findViewById(R.id.sms_message_body); {
			messageBody.setText(smsBody);
		}

		return rootView;
	}

	public SmsMessageView(String smsSender, String smsBody) {
		this.smsSender = smsSender;
		this.smsBody = smsBody;
	}


}
