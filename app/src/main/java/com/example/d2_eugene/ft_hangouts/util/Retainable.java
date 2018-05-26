package com.example.d2_eugene.ft_hangouts.util;

import android.os.Bundle;

public interface Retainable {

	Bundle saveState();

	void restoreFromState(Bundle state);

}
