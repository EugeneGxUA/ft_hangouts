package com.example.d2_eugene.ft_hangouts.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

	private  String serviceProviderNumber;


	final SmsManager smsManager = SmsManager.getDefault();

	public SmsListener listener;

	public SmsBroadcastReceiver(String serviceProviderNumber) {
		this.serviceProviderNumber = serviceProviderNumber;
	}

	public SmsBroadcastReceiver() {}

	private String smsSender;
	private String smsBody;


	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive: AAAAAAAAAAAAA");
		if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
			for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
				smsSender = smsMessage.getDisplayOriginatingAddress();
				smsBody = smsMessage.getMessageBody();
			}
		} else {

			Bundle smsBundle = intent.getExtras();
			if (smsBundle != null) {
				Object[] pdus = (Object[]) smsBundle.get("pdus");
				String format = smsBundle.getString("format");
				if (pdus == null) {
					Log.d(TAG, "onReceive: SmsBundle no pdus key");
					return;
				}

				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < messages.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
					smsBody += messages[i].getMessageBody();
				}
				smsSender = messages[0].getDisplayOriginatingAddress();
			}
		}

		if (smsSender.equals(serviceProviderNumber)) {
			if (listener != null) {
				listener.onTextReceived(smsBody);
			}
		}
	}

	void setListener(SmsListener listener) {
		this.listener = listener;
	}

	public interface SmsListener {
		void onTextReceived(String text);
	}
}
