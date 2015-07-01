package de.hdm.gruppe1.client.report;

import java.io.Serializable;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeViewReport extends Tree implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	TreeItem rootTreeItem = new TreeItem();
	TreeItem tempItem = rootTreeItem;
	
	int anzahl;

	public TreeViewReport(Stueckliste treeViewStueckliste, int anzahl) {
		try {
			
			treeRecursion(treeViewStueckliste, anzahl);
			
			
			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			this.addItem(rootTreeItem);
			
		} catch (Exception e) {
			GWT.log(e.toString());
//			System.out.println(e.toString());
		}
		
	}

	private void treeRecursion(Element element, int anzahl) {
		if(element instanceof Stueckliste) {
			Stueckliste aktuellesStueckliste = (Stueckliste) element;
			rootTreeItem.setText(aktuellesStueckliste.getName());
//			rootTreeItem.setStyleName("treeIntersection");
			
			for(int i = 0; i<aktuellesStueckliste.getBaugruppenPaare().size(); i++){
				Element childBaugruppenElement = aktuellesStueckliste.getBaugruppenPaare().get(i).getElement();
				int anzahlBaugruppe = aktuellesStueckliste.getBaugruppenPaare().get(i).getAnzahl();
				treeRecursion(childBaugruppenElement, anzahlBaugruppe);
			}
			
			for(int i = 0; i<aktuellesStueckliste.getBauteilPaare().size(); i++){
				Element childBauteilElement = aktuellesStueckliste.getBauteilPaare().get(i).getElement();
				int anzahlBauteil = aktuellesStueckliste.getBauteilPaare().get(i).getAnzahl();
				treeRecursion(childBauteilElement, anzahlBauteil);
			}
			
			
			
		}
		if(element instanceof Bauteil) {

			Bauteil aktuellesBauteil = (Bauteil) element;
			
			TreeItem bauteilTreeItem = new TreeItem();
			bauteilTreeItem.setText(anzahl+" * Bauteil: "+aktuellesBauteil.getName());
			rootTreeItem.addItem(bauteilTreeItem);
//			rootTreeItem.removeStyleName("treeIntersection");
						
			
		} else if(element instanceof Baugruppe) {
			//if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			
			TreeItem baugruppeTreeItem = new TreeItem();
			baugruppeTreeItem.setText(anzahl+" * Baugruppe: "+aktuellesBaugruppe.getName());
			
			//TODO: Reihenfolge des baums stimmt noch nicht, tempItem und rootItem an tree anpassen
			tempItem.addItem(baugruppeTreeItem);
//			tempItem.setStyleName("treeIntersection");
			tempItem = baugruppeTreeItem;
			
			for(int i = 0; i<aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().size(); i++){
				Element childBaugruppenElement = aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement();
				int childBaugruppenAnzahl = aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getAnzahl();

				//TreeItem childBaugruppenItem = new TreeItem();
				//childBaugruppenItem.setText(childBaugruppenElement.getName());
				//baugruppeTreeItem.addItem(childBaugruppenItem);
				
				treeRecursion(childBaugruppenElement, childBaugruppenAnzahl);
			}
			

			for(int i = 0; i<aktuellesBaugruppe.getStueckliste().getBauteilPaare().size(); i++){
				Element childBauteilElement = aktuellesBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement();
				TreeItem childBauteilItem = new TreeItem();
				childBauteilItem.setText(aktuellesBaugruppe.getStueckliste().getBauteilPaare().get(i).getAnzahl()
						+" * Bauteil: "+childBauteilElement.getName());
				baugruppeTreeItem.addItem(childBauteilItem);
				
//				rootTreeItem.removeStyleName("treeIntersection");
//				tempItem.setStyleName("treeChild");
				
			}
										
			}
		}
	
}