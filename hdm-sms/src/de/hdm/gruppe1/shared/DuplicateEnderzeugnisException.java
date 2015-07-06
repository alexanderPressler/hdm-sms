package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Enderzeugnis;

public class DuplicateEnderzeugnisException extends SmsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateEnderzeugnisException (){
		
	}
	public DuplicateEnderzeugnisException(Enderzeugnis cause) {
		super(cause);
		this.setMessage("Das Enderzeugnis konnte nicht hinzugefügt werden, da es bereits ein Enderzeugnis mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}
	
}
