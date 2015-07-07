/**
 * 
 */
package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Element;

/**
 * Das System hat individuelle Exception-Klassen, 
 * um die Fehler bei RPC aufrufen zu Klassifizeiren
 * und die Ursache zu ermitteln
 * Hierzu besitzten die Exception eine Error Message (String)
 * und ein Auslöser-Element (Element), dass den Fehler verursacht  
 * @author Andreas Herrmann
 *
 */
public class SmsException extends Exception {


	private static final long serialVersionUID = 1L;
	
	/**
	 * Deklaration der Attribute
	 * message
	 * Element
	 * + Setter & Getter
	 */
	private String message;
	private Element ausloeser;
	
	/**
	 * leerer Konstruktor
	 * @see #SmsException ()
	 */
	public SmsException (){
	}
	
	/**
	 * Konstruktor mit Fehler message
	 * @see #SmsException (String message)
	 */
	public SmsException (String message){
	setMessage(message);
	}
	
	/**
	 * Konstruktor mit Auslöser Element
	 * @see #SmsException (Element cause)
	 */
	public SmsException (Element cause){
		this.ausloeser=cause;
	}
	
	/**
	 * Konstruktor mit Auslöser Element
	 * und Fehler message 
	 * @see #SmsException (String message, Element cause)
	 */
	public SmsException (String message, Element cause){
		this.message=message;
		this.ausloeser=cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Element getAusloeser() {
		return ausloeser;
	}

	public void setAusloeser(Element cause) {
		this.ausloeser = cause;
	}

}
