package com.example.d2_eugene.ft_hangouts.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d2_eugene.ft_hangouts.R;

import java.util.Locale;

public class SettingsActivity extends Activity {

	boolean languageExpand = false;
	boolean colorExpand = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SharedPreferences themePrefs = getSharedPreferences("theme", Context.MODE_PRIVATE); {
			if (themePrefs.getString("theme", "").equals("light")) setTheme(R.style.Custom_light);
			else setTheme(R.style.Custom_dark);
		}
		final SharedPreferences langPrefs = getSharedPreferences("language", Context.MODE_PRIVATE);

		setContentView(R.layout.activity_settings);



		final ViewGroup contentContainer = findViewById(R.id.settings_container); {

			contentContainer.removeAllViews();

			LayoutTransition layoutTransition = new LayoutTransition(); {
				layoutTransition.enableTransitionType(LayoutTransition.APPEARING);
				layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
				layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
				layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
				layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING);

				contentContainer.setLayoutTransition(layoutTransition);
			}

			LayoutInflater inflater = getLayoutInflater();

			final View firstSettingElement = inflater.inflate(R.layout.view_expandable_element, contentContainer, false); {

				final TextView elementTitle = firstSettingElement.findViewById(R.id.title_setting_element); {
					elementTitle.setText(getString(R.string.colors_title));
				}

				final View body = firstSettingElement.findViewById(R.id.setting_body);

				final View dropButton = firstSettingElement.findViewById(R.id.drop_body_button); {
					dropButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
						if (!colorExpand) {
							body.setVisibility(View.VISIBLE);
							colorExpand = true;
						} else {
							body.setVisibility(View.GONE);
							colorExpand = false;
						}
					} });
				}

				final TextView firstElement = firstSettingElement.findViewById(R.id.body_first_text_element); {
					firstElement.setText(getString(R.string.theme_light_name)); {
						firstElement.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
							SharedPreferences.Editor editor = themePrefs.edit();
							editor.putString("theme", "light");
							editor.commit();
							MainActivity.start(SettingsActivity.this);
						} });
					}
				}

				final ImageView firstImageElement = firstSettingElement.findViewById(R.id.body_first_image_element); {
					firstImageElement.setImageResource(R.drawable.gradient_light);
				}

				final TextView secondElement = firstSettingElement.findViewById(R.id.body_second_text_element); {
					secondElement.setText(getString(R.string.theme_dark_name)); {
						secondElement.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								SharedPreferences.Editor editor = themePrefs.edit();
								editor.putString("theme", "dark");
								editor.commit();
								MainActivity.start(SettingsActivity.this);
							}
						});
					}
				}

				final ImageView secondImageElement = firstSettingElement.findViewById(R.id.body_second_image_element); {
					secondImageElement.setImageResource(R.drawable.gradient_dark);
				}

				contentContainer.addView(firstSettingElement);
			}

			final View secondSettingElement = inflater.inflate(R.layout.view_expandable_element, contentContainer, false); {

				final TextView elementTitle = secondSettingElement.findViewById(R.id.title_setting_element); {
					elementTitle.setText(getString(R.string.language_title));
				}

				final View body = secondSettingElement.findViewById(R.id.setting_body);

				final View dropButton = secondSettingElement.findViewById(R.id.drop_body_button); {
					dropButton.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
						if (!languageExpand) {
							body.setVisibility(View.VISIBLE);
							languageExpand = true;
						} else {
							body.setVisibility(View.GONE);
							languageExpand = false;
						}
					} });
				}

				final TextView firstElement = secondSettingElement.findViewById(R.id.body_first_text_element); {
					firstElement.setText(getString(R.string.en_lang));
					firstElement.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
						Resources resources = getResources();
						Configuration configuration = resources.getConfiguration();

						DisplayMetrics dm = resources.getDisplayMetrics();
						Locale locale = new Locale("en");
						Locale.setDefault(locale);

						configuration.setLocale(locale);

						resources.updateConfiguration(configuration, dm);

						SharedPreferences.Editor editor = langPrefs.edit();
						editor.putString("language", "en");
						editor.commit();

						recreate();
					} });
				}

				final ImageView firstImageElement = secondSettingElement.findViewById(R.id.body_first_image_element); {
					firstImageElement.setImageResource(R.mipmap.icon_flag_us);
				}

				final TextView secondElement = secondSettingElement.findViewById(R.id.body_second_text_element); {
					secondElement.setText(getString(R.string.ua_lang));
					secondElement.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
						Resources resources = getResources();

						DisplayMetrics dm = resources.getDisplayMetrics();
						Configuration configuration = resources.getConfiguration();

						Locale locale = new Locale("uk");
						Locale.setDefault(locale);

						configuration.setLocale(locale);

						resources.updateConfiguration(configuration, dm);

						SharedPreferences.Editor editor = langPrefs.edit();
						editor.putString("language", "uk");
						editor.commit();

						recreate();
					} });
				}

				final ImageView secondImageElement = secondSettingElement.findViewById(R.id.body_second_image_element); {
					secondImageElement.setImageResource(R.mipmap.icon_flag_ua);
				}
				contentContainer.addView(secondSettingElement);
			}

		}
	}

	public static void start(Context context) {
		final Intent intent = new Intent(context, SettingsActivity.class);

		context.startActivity(intent);
	}
}
