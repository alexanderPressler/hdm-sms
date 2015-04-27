package de.hdm.gruppe1.shared.bo;

public class Bauteil extends Element {
	
	private static final long serialVersionUID = 1L;
	
	String bauteilBeschreibung = null;
	public String getBauteilBeschreibung() {
		return bauteilBeschreibung;
	}
	public void setBauteilBeschreibung(String bauteilBeschreibung) {
		this.bauteilBeschreibung = bauteilBeschreibung;
	}
	String MaterialBeschreibung = null;
	public String getMaterialBeschreibung() {
		return MaterialBeschreibung;
	}
	public void setMaterialBeschreibung(String materialBeschreibung) {
		MaterialBeschreibung = materialBeschreibung;
	}
	@Override
	public String toString() {
	    return super.toString() + " Elementname: #" + this.name + " Teilbeschreibung: " + this.bauteilBeschreibung;
	  }
}
