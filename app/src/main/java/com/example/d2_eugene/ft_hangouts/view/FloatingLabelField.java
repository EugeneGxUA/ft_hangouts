package com.example.d2_eugene.ft_hangouts.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;
import com.example.d2_eugene.ft_hangouts.util.ValueChangedListener;

import java.util.HashSet;
import java.util.Set;

public class FloatingLabelField extends FrameLayout {

	private String oldValue = "";

	private final TextView hintTextView;
	private final EditText textField;

	private Set<ValueChangedListener> valueChangedListeners = new HashSet<ValueChangedListener>();

	public void addValueChangeListener(ValueChangedListener listener) {
		valueChangedListeners.add(listener);
	}

	{

		Activity activity = (Activity) getContext();

		LayoutInflater layoutInflater = activity.getLayoutInflater();

		final View rootView =  layoutInflater.inflate(R.layout.view_floating_field, this, false);

		hintTextView = rootView.findViewById(R.id.hint_text_view);
		textField = rootView.findViewById(R.id.text_field); {
			textField.addTextChangedListener(new TextWatcher() {
				@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

				@Override public void afterTextChanged(Editable editableField) {

					String value = editableField.toString();

					if (value.isEmpty() != oldValue.isEmpty()) {

						final ValueAnimator animator = ValueAnimator.ofFloat(hintTextView.getScaleX(), value.isEmpty() ? 1f : 0.65f);

						animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { @Override public void onAnimationUpdate(ValueAnimator animation) {
							final float value = (float) animation.getAnimatedValue();
							hintTextView.setScaleX(value);
							hintTextView.setScaleY(value);
						} });

						animator.setDuration(256);
						animator.start();
					}

					final String oldValue = FloatingLabelField.this.oldValue;

					FloatingLabelField.this.oldValue = value;

					for (ValueChangedListener valueChangedListener : valueChangedListeners) {
						valueChangedListener.onValueChanged(oldValue, value);
					}
				}
			});
		}

		addView(rootView);

	}

	public void setErrorState(boolean errorState) {
		if (errorState) {
			hintTextView.setTextColor(Color.RED);
			return;
		}

		hintTextView.setTextColor(getResources().getColor(R.color.black));
	}

	public void setText(String text) {
		textField.setText(text);
	}

	public void setHintText(String hintText) {
		hintTextView.setText(hintText);
	}

	public String getValue() {
		return textField.getText().toString();
	}

	public void setInputType(int inputType) {
		textField.setInputType(inputType);
	}


	public FloatingLabelField(Context context) {
		super(context);
	}

	public FloatingLabelField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatingLabelField(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FloatingLabelField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
}
