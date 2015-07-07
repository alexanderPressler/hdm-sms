package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Bauteil;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * analog zu {@SmsException}  
 * Diese Exception prüft ob der Name eines Bauteils bereits 
 * existiert, und verhindert sonst ein Anlegen des Bauteils
 * 
 * @author Andreas Herrmann
 */
public class DuplicateBauteilException extends SmsException {
	
	private static final long serialVersionUID = 1L;
	
	public DuplicateBauteilException (){
		
	}
	public DuplicateBauteilException(Bauteil cause) {
		super(cause);
		this.setMessage("Das Bauteil konnte nicht hinzugefügt werden, da es bereits ein Bauteil mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}

}
