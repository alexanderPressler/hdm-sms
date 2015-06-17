package de.hdm.gruppe1.shared.bo;

import java.util.Date;
import java.util.Vector;

public class Stueckliste extends Element {

	Vector<ElementPaar> BauteilPaare = new Vector<ElementPaar>();
	Vector<ElementPaar> BaugruppenPaare = new Vector<ElementPaar>();
	Date creationDate = null;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Vector<ElementPaar> getBauteilPaare() {
		return this.BauteilPaare;
	}

	public void setBauteilPaare(Vector<ElementPaar> bauteilPaare) {
		this.BauteilPaare = bauteilPaare;
	}


	public Vector<ElementPaar> getBaugruppenPaare() {
		return this.BaugruppenPaare;
	}

	public void setBaugruppenPaare(Vector<ElementPaar> baugruppenPaare) {
		this.BaugruppenPaare = baugruppenPaare;
	}

}