package de.hdm.gruppe1.shared.bo;

import java.util.Date;

/**
 * Element ist die Oberklasse von Bauteilen und Baugruppen {@link Bauteil @link Baugruppe}.
 * Diese Oberklasse vererbt an genannte 
 * Klassen die Eigenschaften die auch alle Klassen beinhalten. Das ist zB. Name,
 * der bearbeitende Anwender und das Datum der Bearbeitung.Hier werden diese
 * Eigenschaften erzeugt.
 * Ausserdem erbt Element von der Superklasse Business Object, sie ist die Basisklasse 
 * aller in diesemProjekt für die Umsetzung des Fachkonzepts relevanten Klassen dar.
 * </p>
 * @author Schmidt & Pressler
 *
 */
public abstract class Element extends BusinessObject {
	
	//Declaration fuer die Serializible Class
	private static final long serialVersionUID = 1L;
	
	/**
	 * Deklaration der Attribute
	 * name
	 * letzter Bearbeiter 
	 * Bearbeitungsdatum
	 * + Setter & Getter
	 */
	String name = null;
	User editUser = null;
	Date editDate = null; 

	
	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public User getEditUser() {
		return editUser;
	}

	public void setEditUser(User editUser) {
		this.editUser = editUser;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	   * Erzeugen einer einfachen textuellen Repräsentation der jeweiligen
	   * Kontoinstanz.
	   * @see #toString()
	   */
	  @Override
	public String toString() {
	    return super.toString() + " Elementname:" + this.name;
	  }

	  /**
	   * <p>
	   * Feststellen der <em>inhaltlichen</em> Gleichheit zweier Account-Objekte.
	   * Die Gleichheit wird in diesem Beispiel auf eine identische Kontonummer
	   * beschränkt.
	   * </p>
	   * <p>
	   * <b>ACHTUNG:</b> Die inhaltliche Gleichheit nicht mit dem Vergleich der
	   * <em>Identität</em> eines Objekts mit einem anderen verwechseln!!! Dies
	   * würde durch den Operator <code>==</code> bestimmt. Bei Unklarheit hierzu
	   * können Sie nocheinmal in die Definition des Sprachkerns von Java schauen.
	   * Die Methode <code>equals(...)</code> ist für jeden Referenzdatentyp
	   * definiert, da sie bereits in der Klasse <code>Object</code> in einfachster
	   * Form realisiert ist. Dort ist sie allerdings auf die simple Bestimmung der 
	   * Gleicheit der Java-internen Objekt-ID der verglichenen Objekte beschränkt.
	   * In unseren eigenen Klassen können wir diese Methode überschreiben und ihr
	   * mehr Intelligenz verleihen.
	   * </p>
	   * @see #equals(Object)
	   */
	  @Override
	public boolean equals(Object o) {
	    /*
	     * Abfragen, ob ein Objekt ungl. NULL ist und ob ein Objekt gecastet werden
	     * kann, sind immer wichtig!
	     */
	    if (o != null && o instanceof Element) {
	    	Element c = (Element) o;
	      try {
	        return super.equals(c);
	      }
	      catch (IllegalArgumentException e) {
	        return false;
	      }
	    }
	    return false;
	  }
	
	
}
