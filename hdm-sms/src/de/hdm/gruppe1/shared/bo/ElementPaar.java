package de.hdm.gruppe1.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Klasse, um Elemente und deren Anzahl für Stuecklisten zu speichern
 * </p>
 * Diese Klasse bildet aus Elementen der Basisklasse(Stückliste und Baugruppe)
 * zu einem Paar durch Mengenanzahl und Element an sich. Dies ist notwendig um 
 * beim erstellen der z.B Baugruppe{@link CreateBaugruppe}},
 *  {@link CreateStueckliste}}zu sammeln
 *  und für eine Baugruppe oder Stückliste zu speichern.
 * @author Schmidt & Pressler
 */

public class ElementPaar extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Deklaration der Attribute
	 * Anzahl
	 * Element
	 * + Setter & Getter
	 */
	private int anzahl=0;
	private Element element = null;
	
	public int getAnzahl() {
		return anzahl;
	}
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
}
