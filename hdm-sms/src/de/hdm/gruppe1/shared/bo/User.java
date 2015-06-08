package de.hdm.gruppe1.shared.bo;

import de.hdm.gruppe1.shared.bo.BusinessObject;

/**
 * Nutzerklasse, diese Klasse greift auf die Nutzerdaten
 * über die Google Accounts API zu .
 * 
 * @author alex
 * @version 1.0
 */
public class User extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String googleID ="";
	
	public String getGoogleID() {
		return googleID;
	}
	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}