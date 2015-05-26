package de.hdm.gruppe1.shared.bo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.google.appengine.api.users.User;

/**
 * <p>
 * Die Klasse <code>BusinessObject</code> stellt die Basisklasse aller in diesem
 * Projekt für die Umsetzung des Fachkonzepts relevanten Klassen dar.
 * </p>
 * <p>
 * Zentrales Merkmal ist, dass jedes <code>BusinessObject</code> eine Nummer
 * besitzt, die man in einer relationalen Datenbank auch als Primärschlüssel
 * bezeichnen würde. Fernen ist jedes <code>BusinessObject</code> als
 * {@link Serializable} gekennzeichnet. Durch diese Eigenschaft kann jedes
 * <code>BusinessObject</code> automatisch in eine textuelle Form überführt und
 * z.B. zwischen Client und Server transportiert werden. Bei GWT RPC ist diese
 * textuelle Notation in JSON (siehe http://www.json.org/) kodiert.
 * </p>
 * 
 * @author thies
 * @version 1.0
 */

public abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Eindeutige ID alles BOs samt getter und setter.
	 */
	protected int id = 0;


	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @author thies 
	 * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	 * Dies kann selbstverständlich in Subklassen überschrieben werden.
	 */
	@Override
	public String toString() {
		/*
		 * Wir geben den Klassennamen gefolgt von der ID des Objekts zurück.
		 */
		return this.getClass().getName() + " #" + this.id;
	}
	
	  /**
	   * <p>
	   * @author thies 
	   * Feststellen der <em>inhaltlichen</em> Gleichheit zweier
	   * <code>BusinessObject</code>-Objekte. Die Gleichheit wird in diesem Beispiel auf eine
	   * identische ID beschränkt.
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
	   */
	//TODO: was heisst dieses @override 
	  @Override
	public boolean equals(Object o) {
	    /*
	     * Abfragen, ob ein Objekt ungleich NULL ist und ob ein Objekt gecastet
	     * werden kann, sind immer wichtig!
	     */
	    if (o != null && o instanceof BusinessObject) {
	      BusinessObject bo = (BusinessObject) o;
	      try {
	        if (bo.getId() == this.id)
	          return true;
	      }
	      catch (IllegalArgumentException e) {
	        /*
	         * Wenn irgendetwas schief geht, dann geben wir sicherheitshalber false
	         * zurück.
	         */
	        return false;
	      }
	    }
	    /*
	     * Wenn bislang keine Gleichheit bestimmt werden konnte, dann müssen
	     * schließlich false zurückgeben.
	     */
	    return false;
	  }

	  /**
	   *@author thies 
	   * <p>
	   * Erzeugen einer ganzen Zahl, die für das <code>BusinessObject</code> charakteristisch ist.
	   * </p>
	   * <p>
	   * Zusammen mit <code>equals</code> sollte diese Methode immer definiert werden. Manche Java-Klassen
	   * verwendenden <code>hashCode</code>, um initial ein Objekt (z.B. in einer Hashtable) zu identifizieren. Erst danach
	   * würde mit <code>equals</code> festgestellt, ob es sich tatsächlich um das gesuchte Objekt handelt.
	   */
	  @Override
	public int hashCode() {
		  return this.id;
	  }
	  
}
