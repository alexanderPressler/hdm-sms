package de.hdm.gruppe1.shared.bo;

public class Baugruppe extends Element{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * die Baugruppe werden in St�cklisten verwendet, dadruch werden
	 * in die Klassen St�ckliste angelegt und gesetzt.
	 **/
	Stueckliste stueckliste = "";
	
	public getStueckliste(){
		return this.stueckliste;
	}
	public void setStueckliste(Stueckliste stueckliste){
	this.stueckliste = stueckliste;
	}
}
