package de.hdm.gruppe1.shared.bo;

public class Enderzeugnis extends Element {

	private static final long serialVersionUID = 1L;
	Baugruppe baugruppe = new Baugruppe();
	public Baugruppe getBaugruppe() {
		return baugruppe;
	}
	public void setBaugruppe(Baugruppe baugruppe) {
		this.baugruppe = baugruppe;
	}
}
