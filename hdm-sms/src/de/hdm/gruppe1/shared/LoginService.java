package de.hdm.gruppe1.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Synchrones Interface für den Login service Editor
 * 
 * @author Thies, Schmidt & Pressler
 */
@RemoteServiceRelativePath("login")
	public interface LoginService extends RemoteService {

		public LoginInfo getUserInfo(String uri);

	}