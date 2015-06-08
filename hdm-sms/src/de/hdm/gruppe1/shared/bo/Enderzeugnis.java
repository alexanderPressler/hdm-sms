package de.hdm.gruppe1.shared.bo;

import java.util.Vector;

public class Enderzeugnis extends Baugruppe {
 
	private Vector<Baugruppe> zugehoerigenBaugruppe ;
	
	public Baugruppe getBaugruppe(){
	return this.zugehoerigenBaugruppe;
	}
	
	public void setEnderzeugnis(Vector<Baugruppe> baugruppe) {
		this.zugehoerigenBaugruppe= baugruppe;
		
	}
	

}
