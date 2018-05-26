package com.example.d2_eugene.ft_hangouts;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.models.Profile;
import com.example.d2_eugene.ft_hangouts.util.SmsBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ThisApp extends Application{

	private SmsBroadcastReceiver smsBroadcastReceiver;

	@Override
	public void onCreate() {
		super.onCreate();
		smsBroadcastReceiver = new SmsBroadcastReceiver("", "");
		registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
	}

	@Override
	public void onTerminate() {
		unregisterReceiver(smsBroadcastReceiver);
		super.onTerminate();
	}
}
