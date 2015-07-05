package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Stueckliste;

public class DublicateStuecklisteException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DublicateStuecklisteException (){
		
	}
	public DublicateStuecklisteException(Stueckliste cause) {
		super(cause);
		this.setMessage("Die Stückliste konnte nicht hinzugefügt werden, da es bereits eine Stückliste mit diesem Namen in der Datenbank gibt: ");
	}
	
}