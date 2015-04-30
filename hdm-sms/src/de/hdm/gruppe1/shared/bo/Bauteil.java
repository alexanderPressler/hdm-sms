package de.hdm.gruppe1.shared.bo;

public class Bauteil extends Element {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 *Bauteilbeschreibung + die Setzen und Auslesen Methoden 
	 **/
	String bauteilBeschreibung = "leer";
	public String getBauteilBeschreibung() {
		return this.bauteilBeschreibung;
	}
	public void setBauteilBeschreibung(String bauteilBeschreibung) {
		this.bauteilBeschreibung = bauteilBeschreibung;
	}
	/**
	 *Bauteil-Materialbeschreibung + die Setzen und Auslesen Methoden 
	 **/
	String materialBeschreibung = "leer";
	public String getMaterialBeschreibung() {
		return this.materialBeschreibung;
	}
	public void setMaterialBeschreibung(String materialBeschreibung) {
		this.materialBeschreibung = materialBeschreibung;
	}
	
	/**
	   * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	   * Diese besteht aus dem Text, der durch die <code>toString()</code>-Methode
	   * der Superklasse erzeugt wird, ergänzt durch den Name  und Teilbeschreibung des 
	   * jeweiligen Bauteils.
	   */
	@Override
	public String toString() {
	    return super.toString() + " Elementname: #" + this.name + " Teilbeschreibung: " + this.bauteilBeschreibung;
	  }
}
