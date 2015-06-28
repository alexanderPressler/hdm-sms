package de.hdm.gruppe1.client.report;

import java.io.Serializable;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class BaugruppenReport implements Serializable {

	private static final long serialVersionUID = 1L;
	String HTMLString = new String();
	
	public String getHTMLstring() {
		return HTMLString;
	}

	public BaugruppenReport(){
		
	}
	
	public BaugruppenReport(Stueckliste Baugruppenstueckliste){
		
		TreeViewReport stuecklistenTree = new TreeViewReport (Baugruppenstueckliste);
		stuecklistenTree.setStyleName("tree");
		HTMLString = stuecklistenTree.toString();
		
	}
	
}
