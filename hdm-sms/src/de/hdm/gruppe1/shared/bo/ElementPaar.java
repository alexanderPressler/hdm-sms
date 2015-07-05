package de.hdm.gruppe1.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Diese Klasse bildet aus Elementen der Basisklasse(Stückliste und Baugruppe)
 * zu einem Paar durch Mengenanzahl und Element an sich. Dies ist notwendig um 
 * beim erstellen der z.B Baugruppe{@link CreateBaugruppe}},
 *  {@link CreateStueckliste}}zu sammeln
 *  und für eine Baugruppe oder Stückliste zu speichern.
 * 
 *
 * @author Alex und Mario
 * @author Schmidt
 * @version 1.0
 */

public class ElementPaar extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * deklarieren und initialisieren von einen Element und der Anzahl
	 */
	private int anzahl=0;
	private Element element = null;
	
	/**
	 * 
	 * Auslesen der Anzahl.
	 *
	*@return Anzahl der gewünsche Menge für das Element
	*/
	
	public int getAnzahl() {
		return anzahl;
		
	}
	
	/**
	 * Anzahl setzten
	 * @param anzahl
	 */
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
	/**
	 * Auslesen eines Elements
	 */
	public Element getElement() {
		return element;
		
	}
	
	/**
	 * Ein Element setzen
	 */
	public void setElement(Element element) {
		this.element = element;
	}
}