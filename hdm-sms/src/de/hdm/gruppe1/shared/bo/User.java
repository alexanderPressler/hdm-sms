package de.hdm.gruppe1.shared.bo;

import de.hdm.gruppe1.shared.bo.BusinessObject;

/**
 * Nutzerklasse, diese Klasse greift auf die Nutzerdaten
 * über die Google Accounts API zu .
 * 
 * @author Schmidt & Pressler
 */
public class User extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Deklaration der Attribute
	 * name
	 * googleID
	 * + Setter & Getter
	 */
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
