package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Stueckliste;

public class DuplicateStuecklisteException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateStuecklisteException (){
		
	}
	public DuplicateStuecklisteException(Stueckliste cause) {
		super(cause);
		this.setMessage("Die Stückliste konnte nicht hinzugefügt werden, da es bereits eine Stückliste mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}
	
}
