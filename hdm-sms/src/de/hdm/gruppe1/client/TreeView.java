package de.hdm.gruppe1.client;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeView extends VerticalPanel {
	
	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	private final Label HeadlineLabel = new Label("Strukturstückliste");
	private final Label SublineLabel = new Label("Sie können die Strukturstückliste aufklappen und zugehörige Baugruppen- und Bauteile anzeigen.");
	Tree tree = new Tree();
	TreeItem rootTreeItem = new TreeItem();
	
	public TreeView(Stueckliste treeViewStueckliste) {
		try {
			//RootTreeItem erstellen und Anzahl und Namen der Stueckliste als Text hinzufügen
			TreeItem rootTreeItem = new TreeItem();
			rootTreeItem.setText("Stückliste '"+treeViewStueckliste.getName()+"'");
			//Treekursion für alle Bauteile der Stückliste starten
			for(int i=0;i<treeViewStueckliste.getBauteilPaare().size();i++){
				//BauteilPaare in TreeItems verwandeln und der RootTreeItem hinzufügen
				rootTreeItem.addItem(this.treeRecursion(treeViewStueckliste.getBauteilPaare().get(i)));
			}
			//Rekursion für alle Baugruppen der Stückliste durchführen
			for(int j=0; j<treeViewStueckliste.getBaugruppenPaare().size();j++){
				//BaugruppenPaare in TreeItems verwandeln und dem RootTreeItem hinzufügen
				rootTreeItem.addItem(this.treeRecursion(treeViewStueckliste.getBaugruppenPaare().get(j)));
			}
			
			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			this.add(HeadlineLabel);
			this.add(SublineLabel);
			tree.addItem(rootTreeItem);
			this.add(tree);
			
			/**
			 * Diverse css-Formatierungen
			 */
			HeadlineLabel.setStyleName("headline");
			SublineLabel.setStyleName("subline");
			
			/**
			 * Abschließend wird alles dem RootPanel zugeordnet
			 */
			RootPanel.get("content_wrap").add(this);
		
		} catch (Exception e) {
			GWT.log(e.toString());
//			System.out.println(e.toString());
		}
		
	}
	
	TreeItem tempItem = rootTreeItem;
	public TreeItem treeRecursion (ElementPaar elementPaar){
		TreeItem newTreeItem = new TreeItem();
		//Überprüfen ob das ElementPaar ein Bauteil oder eine Baugruppe enthält
		//Bauteil
		if(elementPaar.getElement() instanceof Bauteil){
			//Bauteil TreeItem erstellen
			newTreeItem.setText(elementPaar.getAnzahl()+"*Bauteil '"+elementPaar.getElement().getName()+"'");
		}
		//Baugruppe
		if(elementPaar.getElement() instanceof Baugruppe){
			//Baugruppe TreeItem erstellen
			newTreeItem.setText(elementPaar.getAnzahl()+"*Baugruppe '"+elementPaar.getElement().getName()+"'");
			Baugruppe bg = (Baugruppe) elementPaar.getElement();
			//Rekursion für die Bauteile der Baugruppe starten
			for(int k=0; k<bg.getStueckliste().getBauteilPaare().size(); k++){
				//BauteilPaare in TreeItems verwandeln und hinzufügen
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBauteilPaare().get(k)));
			}
			//Rekursion für die Baugruppen der Baugruppe starten
			for(int l=0; l<bg.getStueckliste().getBaugruppenPaare().size();l++){
				//BaugruppenlPaare in TreeItems verwandeln und hinzufügen
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBaugruppenPaare().get(l)));
			}
		}
		
		//TreeItem zurückgeben
		return newTreeItem;
		
	}
}