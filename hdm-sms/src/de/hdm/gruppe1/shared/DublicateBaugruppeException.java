package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Baugruppe;

public class DublicateBaugruppeException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DublicateBaugruppeException (){
		
	}
	public DublicateBaugruppeException(Baugruppe cause) {
		super(cause);
		this.setMessage("Die Baugruppe konnte nicht hinzugefügt werden, da es bereits eine Baugruppe mit diesem Namen in der Datenbank gibt: ");
	}
	
}
