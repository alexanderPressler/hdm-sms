<<<<<<< HEAD
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
=======
package de.hdm.gruppe1.shared.bo;

import java.util.ArrayList;

public class Stueckliste extends ArrayList {

	//TODO: Abchecken ob diese Attribute nicht durch mehrfachvererbung weiter gegeben werden können
	/**
	 * Eindeutige ID alles BOs samt getter und setter.
	 */
	private int id = 0;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	/**
	 * name der Stueckliste inclusive getter und setter.
	 */
	String name = null;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
>>>>>>> refs/remotes/origin/Alex
