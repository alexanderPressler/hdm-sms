package de.hdm.gruppe1.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.report.TreeViewReport;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeView extends VerticalPanel {
	
	
	
	
	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	private final Label HeadlineLabel = new Label("Strukturstückliste");
	Tree tree = new Tree();
	TreeItem rootTreeItem = new TreeItem();
	
	
	public TreeView(Stueckliste treeViewStueckliste) {
		try {
			Window.alert(treeViewStueckliste.getName());
			treeRecursion(treeViewStueckliste);
			
			
			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			this.add(HeadlineLabel);
			tree.addItem(rootTreeItem);
			this.add(tree);
			
			
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
	private void treeRecursion(Element element) {
		if(element instanceof Stueckliste) {
			Stueckliste aktuellesStueckliste = (Stueckliste) element;
			rootTreeItem.setText(aktuellesStueckliste.getName());
			
			for(int i = 0; i<aktuellesStueckliste.getBaugruppenPaare().size(); i++){
				Element childBaugruppenElement = aktuellesStueckliste.getBaugruppenPaare().get(i).getElement();
				treeRecursion(childBaugruppenElement);
			}
			
			for(int i = 0; i<aktuellesStueckliste.getBauteilPaare().size(); i++){
				Element childBauteilElement = aktuellesStueckliste.getBauteilPaare().get(i).getElement();
				treeRecursion(childBauteilElement);
			}
			
			
			
		}
		if(element instanceof Bauteil) {
			Bauteil aktuellesBauteil = (Bauteil) element;
			
			TreeItem bauteilTreeItem = new TreeItem();
			bauteilTreeItem.setText(aktuellesBauteil.getName());
			rootTreeItem.addItem(bauteilTreeItem);
						
			
		} else if(element instanceof Baugruppe) {
			//if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			
			TreeItem baugruppeTreeItem = new TreeItem();
			baugruppeTreeItem.setText(aktuellesBaugruppe.getName());
			
			//TODO: Reihenfolge des baums stimmt noch nicht, tempItem und rootItem an tree anpassen
			tempItem.addItem(baugruppeTreeItem);
			tempItem = baugruppeTreeItem;
			
			for(int i = 0; i<aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().size(); i++){
				Element childBaugruppenElement = aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement();
				//TreeItem childBaugruppenItem = new TreeItem();
				//childBaugruppenItem.setText(childBaugruppenElement.getName());
				//baugruppeTreeItem.addItem(childBaugruppenItem);
				
				treeRecursion(childBaugruppenElement);
			}
			

			for(int i = 0; i<aktuellesBaugruppe.getStueckliste().getBauteilPaare().size(); i++){
				Element childBauteilElement = aktuellesBaugruppe.getStueckliste().getBauteilPaare().get(i).getElement();
				TreeItem childBauteilItem = new TreeItem();
				childBauteilItem.setText(childBauteilElement.getName());
				baugruppeTreeItem.addItem(childBauteilItem);
				
//				treeRecursion(childBauteilElement);
			}
										
				//TODO implementieren
//				baugruppeTreeItem.setText(treeViewStueckliste.getBaugruppenPaare().get(i).getAnzahl()+" * Baugruppe: "+treeViewStueckliste.getBaugruppenPaare().get(i).getElement().getName());
			}
		}
}