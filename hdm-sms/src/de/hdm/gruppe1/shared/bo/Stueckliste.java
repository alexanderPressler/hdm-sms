package de.hdm.gruppe1.shared.bo;

import java.util.Vector;

public class Stueckliste extends Element {

	Vector<ElementPaar> BauteilPaare = new Vector<ElementPaar>();

	public Vector<ElementPaar> getBauteilPaare() {
		return BauteilPaare;
	}

	public void setBauteilPaare(Vector<ElementPaar> bauteilPaare) {
		BauteilPaare = bauteilPaare;
	}

	Vector<ElementPaar> BaugruppenPaare = new Vector<ElementPaar>();

	public Vector<ElementPaar> getBaugruppenPaare() {
		return this.BaugruppenPaare;
	}

	public void setBaugruppenPaare(Vector<ElementPaar> baugruppenPaare) {
		this.BaugruppenPaare = baugruppenPaare;
	}

}