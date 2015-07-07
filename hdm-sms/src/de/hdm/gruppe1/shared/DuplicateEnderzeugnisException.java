package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Enderzeugnis;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * analog zu {@SmsException}  
 * Diese Exception prüft ob der Name eines Enderzeugnisses bereits 
 * existiert, und verhindert sonst ein Anlegen des Enderzeugnisses
 * 
 * @author Andreas Herrmann
 */
public class DuplicateEnderzeugnisException extends SmsException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateEnderzeugnisException (){
		
	}
	public DuplicateEnderzeugnisException(Enderzeugnis cause) {
		super(cause);
		this.setMessage("Das Enderzeugnis konnte nicht hinzugefügt werden, da es bereits ein Enderzeugnis mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}
	
}
