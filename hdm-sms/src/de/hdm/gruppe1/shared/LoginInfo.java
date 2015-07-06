package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.User;
import java.io.Serializable;

/**
 * Diese Klasse enthält alle Daten über die aktuelle Nutzersession
 * 
 * @author Andreas Herrmann
 *
 */
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = -5207880593956618550L;
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private User user;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getGoogleID(){
		return user.getGoogleID();
	}
}