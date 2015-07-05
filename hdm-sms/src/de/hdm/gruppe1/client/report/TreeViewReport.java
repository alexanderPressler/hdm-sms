package de.hdm.gruppe1.client.report;

import java.io.Serializable;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Element;
import de.hdm.gruppe1.shared.bo.ElementPaar;
import de.hdm.gruppe1.shared.bo.Stueckliste;

public class TreeViewReport extends Tree implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * GUI-Elemente für TreeView initialisieren
	 */
	TreeItem rootTreeItem = new TreeItem();
	TreeItem tempItem = rootTreeItem;
	
	/**
	 * Vektoren, um alle Bauteile und Baugruppen eines Baums aufzunehmen. Nachdem der Baum komplett durchlaufen
	 * wurde und alle dabei aufgetauchten Bauteile, bzw. Baugruppen in diesen beiden Vektoren gesammelt wurden,
	 * werden die Vektoren auf identische Bauteile, bzw. Baugruppen untersucht.
	 */
	Vector<ElementPaar> bauteilAusBaum = new Vector<ElementPaar>();
	Vector<ElementPaar> baugruppeAusBaum = new Vector<ElementPaar>();
	
	/**
	 * In diesen Vektoren stehen nach Durchlaufen der Methode summateBauteile alle Bauteil- bzw. Baugruppenpaare.
	 * Identische Bauteile- bzw. Baugruppen sind bereits zusammen addiert.
	 */
	Vector<ElementPaar> summedBauteile = new Vector<ElementPaar>();
	Vector<ElementPaar> summedBaugruppen = new Vector<ElementPaar>();
	
	int anzahl;

	public TreeViewReport(Stueckliste treeViewStueckliste, int anzahl) {
		try {
			
			treeRecursion(treeViewStueckliste, anzahl);
			summateBauteile(bauteilAusBaum);
			
			
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
			
			/**
			 * Der Vektor bauteilSumme wird mit dem hier vorgefundenen ElementPaar von Bauteil befüllt.
			 */
			ElementPaar bauteilPaar = new ElementPaar();
			bauteilPaar.setAnzahl(anzahl);
			bauteilPaar.setElement(aktuellesBauteil);
			bauteilAusBaum.add(bauteilPaar);
			
		} else if(element instanceof Baugruppe) {
			//if(elment instancaof Stueckliste)
			Baugruppe aktuellesBaugruppe = (Baugruppe) element;
			
			TreeItem baugruppeTreeItem = new TreeItem();
			baugruppeTreeItem.setText(anzahl+" * Baugruppe: "+aktuellesBaugruppe.getName());
			
			/**
			 * Der Vektor baugruppeSumme wird mit dem hier vorgefundenen ElementPaar von Baugruppe befüllt.
			 */
			ElementPaar baugruppePaar = new ElementPaar();
			baugruppePaar.setAnzahl(anzahl);
			baugruppePaar.setElement(aktuellesBaugruppe);
			baugruppeAusBaum.add(baugruppePaar);
			
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
				
				/**
				 * Der Vektor baugruppeSumme wird mit dem hier vorgefundenen ElementPaar von Baugruppe befüllt.
				 */
				ElementPaar baugruppenPaar = new ElementPaar();
				baugruppenPaar.setAnzahl(anzahl);
				baugruppenPaar.setElement(aktuellesBaugruppe);
				baugruppeAusBaum.add(baugruppenPaar);
				
//				rootTreeItem.removeStyleName("treeIntersection");
//				tempItem.setStyleName("treeChild");
				
			}
										
			}
		}
	
	/**
	 * Methode, die auf doppelte Einträge im ErgebnisBaum prüft.
	 * Doppelte Einträge werden identifiziert und zusammen addiert in einem neuen Ergebnis-Vektor gesammelt.
	 * 
	 * @param bauteilAusBaum
	 * @return allBauteile
	 */
	private Vector<ElementPaar> summateBauteile(Vector<ElementPaar> bauteilAusBaum) {
		
		/**
		 * Die for-Schleife durchläuft den Vektor mit allen vorhandenen Bauteilen des Baums.
		 */
		for(int i = 0; i<bauteilAusBaum.size(); i++) {
			
			/**
			 * Die zweite for-Schleife durchläuft den Vektor mit den bereits addierten BauteilPaaren,
			 * damit für jeden Schleifendurchlauf der ersten Schleife alle bereits vorhandenen BauteilPaare
			 * überprüft werden.
			 */
			for(int c = 0; c<summedBauteile.size(); c++) {
				
				/**
				 * Falls die Bauteil-Id bereits vorhanden ist, wird dessen Anzahl mit der bereits bestehenden Anzahl
				 * verrechnet.
				 */
				if(bauteilAusBaum.get(i).getElement().getId() == summedBauteile.get(c).getElement().getId()) {
					
					/**
					 * Hiermit wird die Anzahl des bereits vorhandenen Bauteils mit der des neuen Bauteils verrechnet.
					 */
					int anzahlBauteilInStock = summedBauteile.get(c).getAnzahl();
					int anzahlNewBauteil = bauteilAusBaum.get(i).getAnzahl();
					int anzahlResult = anzahlBauteilInStock+anzahlNewBauteil;
					
					/**
					 * Ein neues BauteilPaar wird initialisiert und mit dem addierten Wert beider Anzahlen befüllt.
					 * Anschließend wird das Element von Bauteil gesetzt und in den Ergebnis-Vektor summedBauteile
					 * geschrieben.
					 */
					ElementPaar dublicateBauteilPaar = new ElementPaar();
					dublicateBauteilPaar.setAnzahl(anzahlResult);
					dublicateBauteilPaar.setElement(bauteilAusBaum.get(i).getElement());
					summedBauteile.add(dublicateBauteilPaar);
							
				}
				
				/**
				 * Sofern das Bauteil aus dem Baum noch nicht im Ergebnis-Vektor summedBauteile vorhanden ist,
				 * wird es ohne weitere Verrechnung der Anzahl direkt dort hinein geschrieben.
				 */
				else {
					
					ElementPaar uniqueBauteilPaar = new ElementPaar();
					uniqueBauteilPaar.setAnzahl(bauteilAusBaum.get(i).getAnzahl());
					uniqueBauteilPaar.setElement(bauteilAusBaum.get(i).getElement());
					summedBauteile.add(uniqueBauteilPaar);
				}
				
			}
			
		}
		
		/**
		 * Das Ergebnis dieser Methode liefert einen Vektor mit allen Bauteilen des Baums.
		 * Identische Bauteile wurden identifiziert und deren Anzahlen addiert.
		 */
		return summedBauteile;
		
	}
	
}
