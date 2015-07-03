package de.hdm.gruppe1.shared.bo;

/**
 * Ein Enderzeugnis ist eine spezielle Baugruppe. Diese Klasse erbt über die
 * Element-Klasse von der Baugruppe die Objekte.
 * 
 * @author Schmidt
 * 
 */
public class Enderzeugnis extends Element {

	private static final long serialVersionUID = 1L;

	/**
	 * neues BaugruppenObjekt erzeugen
	 */
	Baugruppe baugruppe = new Baugruppe();

	/**
	 * 
	 * Auslesen der baugruppe.
	 * 
	 * @return baugruppe
	 */

	public Baugruppe getBaugruppe() {
		return baugruppe;
	}

	/**
	 * Setzen der stueckliste.
	 * 
	 * @param baugruppe
	 */
	public void setBaugruppe(Baugruppe baugruppe) {
		this.baugruppe = baugruppe;
	}
}