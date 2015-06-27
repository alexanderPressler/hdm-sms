package de.hdm.gruppe1.shared.report;

import java.io.Serializable;
import com.google.gwt.user.client.ui.HTML;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class BaugruppenReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
//	StringBuilder HTMLwriter = new StringBuilder();
//	
//	public StringBuilder getHTMLwriter() {
//		return HTMLwriter;
//	}

	public BaugruppenReport(Stueckliste Baugruppenstueckliste){
		
		TreeViewReport stuecklistenTree = new TreeViewReport (Baugruppenstueckliste);
//		HTMLwriter.append(stuecklistenTree.toString());
		
	}
	
	public BaugruppenReport(){};
}
