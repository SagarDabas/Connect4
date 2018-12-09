package com.inoxapps.connect_4;

import com.inoxapps.connect_4.utils.Screen;

public interface ActionResolver {
	public interface PopupCallback {
		public void onClicked(boolean isYesClicked);
	}

	void gameFinish();

	void currentScreen(String screen);

	boolean isNetworkAvailable();

	public void showRateDialogInGamePlay();

	public void callApplovinAdd();

	public void setCurrentScreen(Screen screen);

	public void showPopup(String title, String message, String positiveButton,
			String negativeButton, boolean isCancellable, PopupCallback callback);
}
