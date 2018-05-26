package com.example.d2_eugene.ft_hangouts.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewCreatorWithArgument<Arg> {

	View onCreate(LayoutInflater inflater, ViewGroup container, Arg argument) ;
}
