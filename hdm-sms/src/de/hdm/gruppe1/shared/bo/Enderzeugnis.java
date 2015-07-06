package de.hdm.gruppe1.shared.bo;

/**
 * Ein Enderzeugnis ist eine spezielle Baugruppe. Diese Klasse erbt über die
 * Element-Klasse von der Baugruppe die Objekte.
 * Enderzeugnisse werden nicht von anderen Objekten verwendet.
 * @author Schmidt & Pressler
 * 
 */
public class Enderzeugnis extends Element {

	private static final long serialVersionUID = 1L;
	/**
	 * Deklaration der Attribute
	 * Baugruppe
	 * + Setter & Getter
	 */
	Baugruppe baugruppe = new Baugruppe();
	public Baugruppe getBaugruppe() {
		return baugruppe;
	}
	public void setBaugruppe(Baugruppe baugruppe) {
		this.baugruppe = baugruppe;
	}
}
