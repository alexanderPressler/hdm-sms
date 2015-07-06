package de.hdm.gruppe1.shared.bo;

import java.util.Date;
import java.util.Vector;

/**
 * In Stücklisten werden Bauteile und Baugruppen verwendet.Eine Stückliste kann
 * eine oder mehrer Bauteilen & Baugruppen verwenden.Daher werden wir für die 
 * Objekte eine dynamische Liste einsetzten.
 * @author Schmidt
 *
 */
public class Stueckliste extends Element {

	/**
	 * die liste von Bauteilpaar und Baugruppenpaar wird instanziert und greift 
	 * auf das ElementenPaar
	 * Zusätzlich wird ein erstellungsdatum anbgesetzt.
	 */
	Vector<ElementPaar> BauteilPaare = new Vector<ElementPaar>();
	Vector<ElementPaar> BaugruppenPaare = new Vector<ElementPaar>();
	Date creationDate = null;

	/**
	 * Das Datum an dem die Stueckliste ertstellt wurde, auslesen
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * das Datum setzen
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * das aktuelle BauteilPaar auslesen
	 * @return
	 */
	public Vector<ElementPaar> getBauteilPaare() {
		return this.BauteilPaare;
	}
	/**
	 * Das aktuelle BP setzten
	 * @param bauteilPaare
	 */

	public void setBauteilPaare(Vector<ElementPaar> bauteilPaare) {
		this.BauteilPaare = bauteilPaare;
	}

	/**
	 * das aktuelle BaugruppenPaar auslesen
	 * @return
	 */
	public Vector<ElementPaar> getBaugruppenPaare() {
		return this.BaugruppenPaare;
	}
	/**
	 * Das aktuelle BaugruppenPaar setzten
	 * @param bauteilPaare
	 */

	public void setBaugruppenPaare(Vector<ElementPaar> baugruppenPaare) {
		this.BaugruppenPaare = baugruppenPaare;
	}

}