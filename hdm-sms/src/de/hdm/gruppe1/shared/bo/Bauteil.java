package de.hdm.gruppe1.shared.bo;
/**
 * Realisierung eines exemplarischen Bauteils. 
 * 
 * Ein Bauteil ist das Kleinste Elementare Teil des Stuecklisten-Systems.
 * Es besitzt eine Beschreibung und eine zusätzliche Materialbeschreibung
 * @author Schmidt & Pressler 
 * 
 */
public class Bauteil extends Element {

	private static final long serialVersionUID = 1L;

	/**
	 * Deklaration der Attribute
	 * Bauteilbeschreibung
	 * Materialbeschreibung
	 * + Setter & Getter
	 */
	String bauteilBeschreibung = "leer";
	String materialBeschreibung = "leer";

	public String getBauteilBeschreibung() {
		return this.bauteilBeschreibung;
	}

	public void setBauteilBeschreibung(String bauteilBeschreibung) {
		this.bauteilBeschreibung = bauteilBeschreibung;
	}

	public String getMaterialBeschreibung() {
		return this.materialBeschreibung;
	}
	public void setMaterialBeschreibung(String materialBeschreibung) {
		this.materialBeschreibung = materialBeschreibung;
	}

	/**
	 * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	 * Diese besteht aus dem Text, der durch die <code>toString()</code>-Methode
	 * der Superklasse erzeugt wird, ergänzt durch den Name und Teilbeschreibung
	 * des jeweiligen Bauteils.
	 */
	@Override
	public String toString() {
		return super.toString() + " Teilbeschreibung: "
				+ this.bauteilBeschreibung + "Materialbeschreibung"
				+ this.materialBeschreibung;
	}
}
