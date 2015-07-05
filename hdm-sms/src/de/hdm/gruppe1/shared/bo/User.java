package de.hdm.gruppe1.shared.bo;

import de.hdm.gruppe1.shared.bo.BusinessObject;

/**
 * Nutzerklasse, diese Klasse greift auf die Nutzerdaten
 * über die Google Accounts API zu .
 * 
 * @author alex & schmidt
 * @version 1.0
 */
public class User extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String googleID ="";
	
	/**
	 * Auslesen der GoogleId
	 * @return
	 */
	public String getGoogleID() {
		return googleID;
	}
	/**
	 * ID setzten
	 * @param googleID
	 */
	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}
	/**
	 * auslesen den Namen des Anwender
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * Username setzen
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}