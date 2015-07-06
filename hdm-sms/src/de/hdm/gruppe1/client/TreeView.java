package de.hdm.gruppe1.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
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
	
	/**
	 * Vektoren, um alle Bauteile und Baugruppen eines Baums aufzunehmen. Nachdem der Baum komplett durchlaufen
	 * wurde und alle dabei aufgetauchten Bauteile, bzw. Baugruppen in diesen beiden Vektoren gesammelt wurden,
	 * werden die Vektoren auf identische Bauteile, bzw. Baugruppen untersucht.
	 */
	private Vector<ElementPaar> bauteilAusBaum = new Vector<ElementPaar>();
	private Vector<ElementPaar> baugruppeAusBaum = new Vector<ElementPaar>();
	
	int anzahl;
	
	public TreeView(Stueckliste treeViewStueckliste) {
		try {
			treeRecursion(treeViewStueckliste,1);
			
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
			
//			/**
//			 * Der Vektor bauteilSumme wird mit dem hier vorgefundenen ElementPaar von Bauteil befüllt.
//			 */
//			ElementPaar bauteilPaar = new ElementPaar();
//			bauteilPaar.setAnzahl(anzahl);
//			bauteilPaar.setElement(aktuellesBauteil);
//			bauteilAusBaum.add(bauteilPaar);
			
		} else if(element instanceof Baugruppe) {
			//if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			
			TreeItem baugruppeTreeItem = new TreeItem();
			baugruppeTreeItem.setText(anzahl+" * Baugruppe: "+aktuellesBaugruppe.getName());
			
//			/**
//			 * Der Vektor baugruppeSumme wird mit dem hier vorgefundenen ElementPaar von Baugruppe befüllt.
//			 */
//			ElementPaar baugruppePaar = new ElementPaar();
//			baugruppePaar.setAnzahl(anzahl);
//			baugruppePaar.setElement(aktuellesBaugruppe);
//			baugruppeAusBaum.add(baugruppePaar);
			
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
				
//				/**
//				 * Der Vektor baugruppeSumme wird mit dem hier vorgefundenen ElementPaar von Baugruppe befüllt.
//				 */
//				ElementPaar baugruppenPaar = new ElementPaar();
//				baugruppenPaar.setAnzahl(anzahl);
//				baugruppenPaar.setElement(aktuellesBaugruppe);
//				baugruppeAusBaum.add(baugruppenPaar);
				
//				rootTreeItem.removeStyleName("treeIntersection");
//				tempItem.setStyleName("treeChild");
				
			}
										
			}
		}
}