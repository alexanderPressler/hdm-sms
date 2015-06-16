/**
 * 
 */
package de.hdm.gruppe1.shared;

import de.hdm.gruppe1.shared.bo.Element;

/**
 * @author Andreas Herrmann
 *
 */
public class ReferenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private Element ausloeser;
	
	public ReferenceException (String message){
	setMessage(message);
	}
	
	public ReferenceException (Element cause){
		this.ausloeser=cause;
	}
	
	public ReferenceException (String message, Element cause){
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