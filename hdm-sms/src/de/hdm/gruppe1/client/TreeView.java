package de.hdm.gruppe1.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeView extends VerticalPanel {
	
	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	private final Label HeadlineLabel = new Label("Strukturstückliste");
	Tree tree = new Tree();
	
	public TreeView(Stueckliste treeViewStueckliste) {
		
		//Knotenpunkte der TreeView
		TreeItem stueckliste = new TreeItem();
		
		//Name der Stückliste reinschreiben
		stueckliste.setText(treeViewStueckliste.getName());
		
		if(treeViewStueckliste.getBaugruppenPaare()!=null){
			
			//TODO für Baugruppen rekursive Tiefensuche implementieren
			
			for(int i = 0; i<treeViewStueckliste.getBaugruppenPaare().size(); i++){
				TreeItem baugruppe = new TreeItem();
				baugruppe.setText(treeViewStueckliste.getBaugruppenPaare().get(i).getAnzahl()+" * Baugruppe: "+treeViewStueckliste.getBaugruppenPaare().get(i).getElement().getName());
				stueckliste.addItem(baugruppe);
			}
			
		}
		
		if(treeViewStueckliste.getBauteilPaare()!=null){
			
			for(int a = 0; a<treeViewStueckliste.getBauteilPaare().size(); a++){
				TreeItem bauteil = new TreeItem();
				bauteil.setText(treeViewStueckliste.getBauteilPaare().get(a).getAnzahl()+" * Bauteil: "+treeViewStueckliste.getBauteilPaare().get(a).getElement().getName());
				stueckliste.addItem(bauteil);
			}
			
		}
		
		tree.addItem(stueckliste);

		/**
		 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
		this.add(HeadlineLabel);
		this.add(tree);
		
		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
		RootPanel.get("content_wrap").add(this);
		
	}
	
	/*
	 * Click Handlers.
	 */

}
