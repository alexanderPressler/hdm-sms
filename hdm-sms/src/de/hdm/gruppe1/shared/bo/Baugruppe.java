package de.hdm.gruppe1.shared.bo;

public class Baugruppe extends Element{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * die Baugruppe werden in Stücklisten verwendet, dadruch werden
	 * in die Klassen Stückliste angelegt und gesetzt.
	 **/
	Stueckliste stueckliste;
	
	public Stueckliste getStueckliste(){
		return this.stueckliste;
	}
	public void setStueckliste(Stueckliste stueckliste){
	this.stueckliste = stueckliste;
	}
	  /**
	   * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	   * Diese besteht aus dem Text, der durch die <code>toString()</code>-Methode
	   * der Superklasse erzeugt wird, ergaenzt durch die verwendete Stueckliste.
	   * der Baugruppe.
	   */
	  public String toString() {
	    return super.toString() + " " + this.stueckliste ;
	  }

	}

