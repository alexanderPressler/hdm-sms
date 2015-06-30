package de.hdm.gruppe1.shared.bo;

import java.util.Date;


public abstract class Element extends BusinessObject {
	
	//Declaration fuer die Serializible Class
	private static final long serialVersionUID = 1L;
	
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
	   * Erzeugen einer einfachen textuellen Repr�sentation der jeweiligen
	   * Kontoinstanz.
	   */
	  @Override
	public String toString() {
	    return super.toString() + " Elementname:" + this.name;
	  }

	  /**
	   * <p>
	   * Feststellen der <em>inhaltlichen</em> Gleichheit zweier Account-Objekte.
	   * Die Gleichheit wird in diesem Beispiel auf eine identische Kontonummer
	   * beschr�nkt.
	   * </p>
	   * <p>
	   * <b>ACHTUNG:</b> Die inhaltliche Gleichheit nicht mit dem Vergleich der
	   * <em>Identit�t</em> eines Objekts mit einem anderen verwechseln!!! Dies
	   * w�rde durch den Operator <code>==</code> bestimmt. Bei Unklarheit hierzu
	   * k�nnen Sie nocheinmal in die Definition des Sprachkerns von Java schauen.
	   * Die Methode <code>equals(...)</code> ist f�r jeden Referenzdatentyp
	   * definiert, da sie bereits in der Klasse <code>Object</code> in einfachster
	   * Form realisiert ist. Dort ist sie allerdings auf die simple Bestimmung der 
	   * Gleicheit der Java-internen Objekt-ID der verglichenen Objekte beschr�nkt.
	   * In unseren eigenen Klassen k�nnen wir diese Methode �berschreiben und ihr
	   * mehr Intelligenz verleihen.
	   * </p>
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