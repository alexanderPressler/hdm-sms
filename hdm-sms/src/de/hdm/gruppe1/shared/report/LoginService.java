package de.hdm.gruppe1.shared.report;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Synchrones Interface für den Login service im Report
 * 
 * @author Thies, Schmidt & Pressler
 */
@RemoteServiceRelativePath("login")
	public interface LoginService extends RemoteService {

		public LoginInfo getUserInfo(String uri);

	}
