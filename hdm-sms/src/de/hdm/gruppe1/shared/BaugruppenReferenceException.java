package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Baugruppe;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * analog zu {@SmsException}  
 * Diese Exception verhindert, dass Baugruppen sich selbst
 * hinzugefügt werden können, da ansonsten ein Loop entstehen würde
 * 
 * @author Andreas Herrmann
 */
public class BaugruppenReferenceException extends SmsException {

	private static final long serialVersionUID = 1L;
	
	public BaugruppenReferenceException (){
		
	}
	public BaugruppenReferenceException(Baugruppe cause) {
		super(cause);
		this.setMessage("Die Baugruppe konnte nicht hinzugefügt werden, da ansonsten ein Loop entstehen würde");
	}

}
