package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Bauteil;

public class DuplicateBauteilException extends SmsException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateBauteilException (){
		
	}
	public DuplicateBauteilException(Bauteil cause) {
		super(cause);
		this.setMessage("Das Bauteil konnte nicht hinzugefügt werden, da es bereits ein Bauteil mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}

}