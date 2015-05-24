package de.hdm.gruppe1.shared.bo;

import java.util.Vector;


public class Stueckliste extends Element {

	//TODO: Abchecken ob diese Attribute nicht durch mehrfachvererbung weiter gegeben werden können
	/**
	 * Eindeutige ID alles BOs samt getter und setter.
	 */
	private int id = 0;
	Vector<ElementPaar> BauteilPaare = new Vector <ElementPaar>();
	Vector<ElementPaar> BaugruppenPaare = new Vector <ElementPaar>();

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
