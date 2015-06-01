package de.hdm.gruppe1.shared.bo;

import java.util.Vector;

public class Baugruppe extends Element{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * die Baugruppe werden mitunter in den St�cklisten verwendet, dadurch werden
	 * in die Klassen St�ckliste angelegt und gesetzt.
	 **/
	Vector<Baugruppe> enthalteneElemente;
	
	public Vector<Baugruppe> getBaugruppe(){
		return this.enthalteneElemente;
	}
	public void setBaugruppe(Vector<Baugruppe> baugruppe){
	this.enthalteneElemente = baugruppe;
	}
	
	String name;
	
	public String getName(){
		return this.name;
	}
		public void setName(String name){
		this.name = name;
	}
		public void add(Baugruppe deleteBaugruppe) {
			// TODO Auto-generated method stub
			
		}
		
		
	  /**
	   * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	   * Diese besteht aus dem Text, der durch die <code>toString()</code>-Methode
	   * der Superklasse erzeugt wird, ergaenzt durch die verwendete Stueckliste.
	   * der Baugruppe.
	   */
	
	//  public String toString() {
	//    return super.toString() + " Elementname: #" + this.name + " " + this.stueckliste ;
	//  }


	}

