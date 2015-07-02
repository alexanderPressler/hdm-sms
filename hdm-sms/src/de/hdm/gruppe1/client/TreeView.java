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
	TreeItem tempItem = new TreeItem();
	
	
	public TreeView(Stueckliste treeViewStueckliste) {
		try {
			Window.alert(treeViewStueckliste.getName());
			this.add(HeadlineLabel);
			
			//Root des Baums setzen
			rootTreeItem.setText(treeViewStueckliste.getName());
			//Bauteilkinder werden ausgelesen und dem baum angehängt
			for(int i = 0; i<treeViewStueckliste.getBauteilPaare().size(); i++){
				Element childBauteilElement = treeViewStueckliste.getBauteilPaare().get(i).getElement();
				TreeItem bauteilTreeItem = new TreeItem();
				bauteilTreeItem.setText(childBauteilElement.getName());
				rootTreeItem.addItem(bauteilTreeItem);
			}
			//Baugruppenkinder werden ausgelesen und dem baum angehängt
			for(int i = 0; i<treeViewStueckliste.getBaugruppenPaare().size(); i++){
				Element childBaugruppenElement = treeViewStueckliste.getBaugruppenPaare().get(i).getElement();
				TreeItem baugruppeTreeItem = new TreeItem();
				baugruppeTreeItem.setText(childBaugruppenElement.getName());
				
					//die Kinder (weitere Bauteile) der Baugruppenkinder werden ausgelesenund dem baum angehängt
					Baugruppe childBaugruppe = (Baugruppe) childBaugruppenElement;
					for(int u = 0; u<childBaugruppe.getStueckliste().getBauteilPaare().size(); u++){
						Element childOfChildBauteilElement = childBaugruppe.getStueckliste().getBauteilPaare().get(u).getElement();
						TreeItem childOfChildBauteilItem = new TreeItem();
						childOfChildBauteilItem.setText(childOfChildBauteilElement.getName());
						baugruppeTreeItem.addItem(childOfChildBauteilItem);
						}
					//die Kinder (weitere gruppen) der Bauteilkinder werden ausgelesenund dem baum angehängt
					for(int o = 0; o<childBaugruppe.getStueckliste().getBaugruppenPaare().size(); o++){
						Element childOfChildBaugruppeElement = childBaugruppe.getStueckliste().getBaugruppenPaare().get(o).getElement();
						TreeItem childOfChildBaugruppeItem = new TreeItem();
						childOfChildBaugruppeItem.setText(childOfChildBaugruppeElement.getName());
						
								//die KindesKinder (weitere Bauteile) der Baugruppenkinder werden ausgelesenund dem baum angehängt
								Baugruppe childofchildBaugruppe = (Baugruppe) childOfChildBaugruppeElement;
								for(int u = 0; u<childofchildBaugruppe.getStueckliste().getBauteilPaare().size(); u++){
									Element childOfChildBauteilElement = childofchildBaugruppe.getStueckliste().getBauteilPaare().get(u).getElement();
									TreeItem childOfChildBauteilItem = new TreeItem();
									childOfChildBauteilItem.setText(childOfChildBauteilElement.getName());
									childOfChildBaugruppeItem.addItem(childOfChildBauteilItem);
									}
								
						baugruppeTreeItem.addItem(childOfChildBaugruppeItem);
				}
				
				rootTreeItem.addItem(baugruppeTreeItem);
			}
			
			
			
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
	
}