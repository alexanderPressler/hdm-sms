package de.hdm.gruppe1.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	TreeItem previousTreeItem = new TreeItem();
	
	
	public TreeView(Stueckliste treeViewStueckliste) {
		try {
			Window.alert(treeViewStueckliste.getName());
			treeRecursion(treeViewStueckliste);
			
			/**
			 * Bei Instantiierung der Klasse wird alles dem VerticalPanel
			 * zugeordnet, da diese Klasse von VerticalPanel erbt.
			 */
			this.add(HeadlineLabel);
			this.add(tree);
			
			tree.addItem(rootTreeItem);
			/**
			 * Abschließend wird alles dem RootPanel zugeordnet
			 */
			RootPanel.get("content_wrap").add(this);
		
		} catch (Exception e) {
//			System.out.println(e.toString());
		}
		
	}
	
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
						
			
		} else {
			//if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			for(int i = 0; i<aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().size(); i++){
				
				TreeItem baugruppeTreeItem = new TreeItem();
				baugruppeTreeItem.setText(aktuellesBaugruppe.getStueckliste().getBaugruppenPaare().get(i).getElement().getName());
				previousTreeItem.addItem(baugruppeTreeItem);
				
				previousTreeItem = baugruppeTreeItem;
				treeRecursion(aktuellesBaugruppe);
				
				
				//TODO implementieren
//				baugruppeTreeItem.setText(treeViewStueckliste.getBaugruppenPaare().get(i).getAnzahl()+" * Baugruppe: "+treeViewStueckliste.getBaugruppenPaare().get(i).getElement().getName());
			}
		}
	}
	
}
