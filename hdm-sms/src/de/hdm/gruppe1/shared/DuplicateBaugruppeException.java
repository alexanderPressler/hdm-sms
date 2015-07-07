package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Baugruppe;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * analog zu {@SmsException}  
 * Diese Exception prüft ob der Name einer Baugruppe bereits 
 * existiert, und verhindert sonst ein Anlegen der Baugruppe
 * 
 * @author Andreas Herrmann
 */
public class DuplicateBaugruppeException extends SmsException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateBaugruppeException (){
		
	}
	public DuplicateBaugruppeException(Baugruppe cause) {
		super(cause);
		this.setMessage("Die Baugruppe konnte nicht hinzugefügt werden, da es bereits eine Baugruppe mit diesem Namen in der Datenbank gibt: '"
				+cause.getName()+"'");
	}
	
}
