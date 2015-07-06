package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Baugruppe;

public class DuplicateBaugruppeException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateBaugruppeException (){
		
	}
	public DuplicateBaugruppeException(Baugruppe cause) {
		super(cause);
		this.setMessage("Die Baugruppe konnte nicht hinzugefügt werden, da es bereits eine Baugruppe mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}
	
}