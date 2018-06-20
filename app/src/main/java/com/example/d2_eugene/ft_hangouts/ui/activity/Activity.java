package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.d2_eugene.ft_hangouts.ThisApp;

import java.util.Date;

public abstract class Activity extends android.app.Activity {

	@Override
	protected void onResume() {
		super.onResume();

		long time = ThisApp.get(this).getLastActivityTime();
		if (time != 0) {
			if ((System.currentTimeMillis() - time) > 500) {
				Toast.makeText(this, new Date(time).toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ThisApp.get(this).onActivityPaused();
	}
}
