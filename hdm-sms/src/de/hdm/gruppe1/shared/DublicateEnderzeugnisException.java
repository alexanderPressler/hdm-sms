package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Enderzeugnis;

public class DublicateEnderzeugnisException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DublicateEnderzeugnisException (){
		
	}
	public DublicateEnderzeugnisException(Enderzeugnis cause) {
		super(cause);
		this.setMessage("Das Enderzeugnis konnte nicht hinzugefügt werden, da es bereits ein Enderzeugnis mit diesem Namen in der Datenbank gibt: ");
	}
	
}
