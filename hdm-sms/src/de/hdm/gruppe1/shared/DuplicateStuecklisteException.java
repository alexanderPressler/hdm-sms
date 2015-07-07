package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Stueckliste;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * analog zu {@SmsException}  
 * Diese Exception prüft ob der Name eine Stückliste bereits 
 * existiert, und verhindert sonst ein Anlegen der Stueckliste
 * 
 * @author Andreas Herrmann
 */
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
