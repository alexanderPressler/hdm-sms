package de.hdm.gruppe1.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Klasse, um Elemente und deren Anzahl für Stuecklisten zu speichern
 * </p>
 * 
 * @author Alex und Mario
 * @version 1.0
 */

public class ElementPaar extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
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