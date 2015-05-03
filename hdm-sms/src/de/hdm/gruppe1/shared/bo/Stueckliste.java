package de.hdm.gruppe1.shared.bo;

public class Stueckliste {

	
	
	
	/**
	   * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	   * Diese besteht aus dem Text, der durch die <code>toString()</code>-Methode
	   * der Superklasse erzeugt wird, ergänzt durch die die spezielle Baugrupper
	   * des Enderzeugnisses.
	   */
	  public String toString() {
	    return super.toString() + " " + this.zugehoerigeBaugruppe;
	  }
}
