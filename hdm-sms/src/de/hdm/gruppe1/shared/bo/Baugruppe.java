package de.hdm.gruppe1.shared.bo;

public class Baugruppe extends Element {

	private static final long serialVersionUID = 1L;
	
	private Stueckliste stueckliste;

	public Stueckliste getStueckliste() {
		return this.stueckliste;
	}

	public void setStueckliste(Stueckliste stueckliste) {
		this.stueckliste = stueckliste;
	}  

}
