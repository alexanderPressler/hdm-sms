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

	private static final long serialVersionUID = 1L;

	/**
	 * Deklaration der Eigenschaften
	 */
	String name = null;
	User editUser = null;
	Date editDate = null;

	/**
	 * 
	 * Auslesen des Datums der Bearbeitung.
	 * 
	 * @return editDate
	 */
	public Date getEditDate() {
		return editDate;
	}

	/**
	 * Setzen des Datum der Bearbeitung.
	 */
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	/**
	 * 
	 * Auslesen des Anwenders, der die Änderung vornommen hat(erstellen, löschen
	 * oder zum Beispiel bearbeiten)
	 * 
	 * @return editUser
	 */
	public User getEditUser() {
		return editUser;
	}

	/**
	 * Anwender setzen.
	 */
	public void setEditUser(User editUser) {
		this.editUser = editUser;
	}

	/**
	 * 
	 * Auslesen des Names
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Name des Elements setzen.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Erzeugen einer einfachen textuellen Repräsentation der jeweiligen
	 * Element-instanz.
	 * @see #toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " Elementname:" + this.name;
	}

	/**
	 * <p>
	 * Feststellen der <em>inhaltlichen</em> Gleichheit zweier Element-Objekte.
	 * Die Gleichheit wird in diesem Beispiel auf eine identische Elemente
	 * beschränkt.
	 * </p>
	 * <p>
	 * Die Methode <code>equals(...)</code> ist für jeden Referenzdatentyp
	 * definiert, da sie bereits in der Klasse <code>Object</code> in
	 * einfachster Form realisiert ist. Dort ist sie allerdings auf die simple
	 * Bestimmung der Gleicheit der Java-internen Objekt-ID der verglichenen
	 * Objekte beschränkt. In unseren eigenen Klassen können wir diese Methode
	 * überschreiben und ihr mehr Intelligenz verleihen.
	 * </p>
	 * @see #equals(Object)
	 */
	@Override
	public boolean equals(Object o) {

		/**
		 * Abfragen, ob ein Objekt ungleich NULL ist und ob ein Objekt gecastet
		 * werden kann, sind immer wichtig!
		 */
		if (o != null && o instanceof Element) {
			Element c = (Element) o;
			try {
				return super.equals(c);
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		return false;
	}

}
