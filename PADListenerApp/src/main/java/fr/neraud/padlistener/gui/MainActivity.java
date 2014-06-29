package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;

/**
 * Main activity
 *
 * @author Neraud
 */
public class MainActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

}