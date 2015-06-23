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
		TreeItem baugruppe = new TreeItem();
		TreeItem bauteil = new TreeItem();
		
		//Name der Stückliste reinschreiben
		stueckliste.addTextItem(""+treeViewStueckliste.getName());
		baugruppe.addTextItem("Inhalt: "+treeViewStueckliste.getBaugruppenPaare().get(1).getElement().getName());
		bauteil.addTextItem("Inhalt: "+treeViewStueckliste.getBauteilPaare().get(1).getElement().getName());
		
		//Unterpunkte von stueckliste wird dem darüber geordneten Tree hinzugefügt
		stueckliste.addItem(bauteil);
		stueckliste.addItem(baugruppe);
		
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
