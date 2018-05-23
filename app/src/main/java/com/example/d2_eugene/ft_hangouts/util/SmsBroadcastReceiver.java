package com.example.d2_eugene.ft_hangouts.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

	private final String serviceProviderNumber;
	private final String serviceProviderSmsCondition;

	private SmsListener listener;

	public SmsBroadcastReceiver(String serviceProviderNumber, String serviceProviderSmsCondition) {
		this.serviceProviderNumber = serviceProviderNumber;
		this.serviceProviderSmsCondition = serviceProviderSmsCondition;
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		String smsSender = "";
		String smsBody = "";
		if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {


			for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
				smsSender = smsMessage.getDisplayOriginatingAddress();
				smsBody = smsMessage.getMessageBody();
			}
		} else {
			Bundle smsBundle = intent.getExtras();
			if (smsBundle != null) {
				Object[] pdus = (Object[]) smsBundle.get("pdus");
				if (pdus == null) {
					Log.d(TAG, "onReceive: SmsBundle no pdus key");
					return;
				}
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < messages.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					smsBody += messages[i].getMessageBody();
				}
				smsSender = messages[0].getDisplayOriginatingAddress();
			}
		}

		if (smsSender.equals(serviceProviderNumber) && smsBody.startsWith(serviceProviderSmsCondition)) {
			if (listener != null) {
				listener.onTextReceived(smsBody);
			}
		}
	}

	void setListener(SmsListener listener) {
		this.listener = listener;
	}

	interface SmsListener {
		void onTextReceived(String text);
	}
}
