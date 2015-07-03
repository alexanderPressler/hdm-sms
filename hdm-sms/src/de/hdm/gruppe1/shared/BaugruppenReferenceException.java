package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Baugruppe;

public class BaugruppenReferenceException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaugruppenReferenceException(){
	}
	
	public BaugruppenReferenceException(Baugruppe cause) {
		super(cause);
		this.setMessage("Die Baugruppe '"+cause.getName()+"' konnte nicht hinzugefügt werden, da ansonsten ein Datenbank-Loop entstehen würde.");
	}

}