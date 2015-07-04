package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void getUserInfo(String uri, AsyncCallback<LoginInfo>callback);

}