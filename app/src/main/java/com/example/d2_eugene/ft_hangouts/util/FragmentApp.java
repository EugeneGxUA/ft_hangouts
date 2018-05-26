package com.example.d2_eugene.ft_hangouts.util;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.d2_eugene.ft_hangouts.annotation.NotNull;
import com.example.d2_eugene.ft_hangouts.annotation.ToOverride;

public class FragmentApp extends Fragment implements Retainable {

	private Activity activity;

	@SuppressWarnings("unchecked")
	public <ActivityType extends Activity> ActivityType getParentActivity() {
		final Activity activity = (Activity) getActivity();
		if (activity != null) this.activity = activity;

		return (ActivityType) this.activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.activity = (Activity) getActivity();
	}

	public void runOnUiThread(Runnable runnable) {
		getParentActivity().runOnUiThread(runnable);
	}

	public void onBackPressed() {
		getParentActivity().onBackPressed();
	}

	public LayoutInflater getActivityLayoutInflater() {
		return getParentActivity().getLayoutInflater();
	}

	@ToOverride
	protected void onResume(@NotNull Bundle state) {}


	@ToOverride
	public Bundle saveState() { return new Bundle(); }

	@ToOverride
	public void restoreFromState(Bundle state) {}


	public static final byte BACK_STACK_IGNORE 	= 0;
	public static final byte BACK_STACK_ADD 	= 1;
	public static final byte BACK_STACK_CLEAR	= 2;
}
