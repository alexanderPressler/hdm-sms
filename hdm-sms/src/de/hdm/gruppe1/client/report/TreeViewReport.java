package de.hdm.gruppe1.client.report;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.ElementPaar;;

public class TreeViewReport extends Tree {
	//Stacks für Bauteile und Baugruppen für die Addition derer
	private Vector<ElementPaar> bauteile = new Vector<ElementPaar>();
	private LinkedList<ElementPaar> baugruppen = new LinkedList<ElementPaar>();
	
	public TreeViewReport (Stueckliste stueckliste, int anzahl){
		//RootTreeItem erstellen und Anzahl und Namen der Stueckliste als Text hinzufügen
		TreeItem rootTreeItem = new TreeItem();
		rootTreeItem.setText("Stückliste '"+stueckliste.getName()+"'");
		//Treekursion für alle Bauteile der Stückliste starten
		for(int i=0;i<stueckliste.getBauteilPaare().size();i++){
			//BauteilPaare in TreeItems verwandeln und der RootTreeItem hinzufügen
			rootTreeItem.addItem(this.treeRecursion(stueckliste.getBauteilPaare().get(i), anzahl));
		}
		//Rekursion für alle Baugruppen der Stückliste durchführen
		for(int j=0; j<stueckliste.getBaugruppenPaare().size();j++){
			//BaugruppenPaare in TreeItems verwandeln und dem RootTreeItem hinzufügen
			rootTreeItem.addItem(this.treeRecursion(stueckliste.getBaugruppenPaare().get(j), anzahl));
		}
		//RootTreeItem dem Baum hinzufügen
		this.addItem(rootTreeItem);
	}
	
	public TreeItem treeRecursion (ElementPaar elementPaar, int anzahl){
		TreeItem newTreeItem = new TreeItem();
		//Anzahl mit der Anzahl im ElementPaar multiplizieren
		int multi = anzahl*elementPaar.getAnzahl();
		//Überprüfen ob das ElementPaar ein Bauteil oder eine Baugruppe enthält
		//Bauteil
		if(elementPaar.getElement() instanceof Bauteil){
			//Bauteil TreeItem erstellen
			newTreeItem.setText(elementPaar.getAnzahl()+"*Bauteil '"+elementPaar.getElement().getName()+"'");
			//Anzahl aktualisieren
			elementPaar.setAnzahl(multi);
			//Dem BauteilStack hinzufügen
			bauteile.add(elementPaar);
		}
		//Baugruppe
		if(elementPaar.getElement() instanceof Baugruppe){
			//Baugruppe TreeItem erstellen
			newTreeItem.setText(elementPaar.getAnzahl()+"*Baugruppe '"+elementPaar.getElement().getName()+"'");
			Baugruppe bg = (Baugruppe) elementPaar.getElement();
			//Rekursion für die Bauteile der Baugruppe starten
			for(int k=0; k<bg.getStueckliste().getBauteilPaare().size(); k++){
				//BauteilPaare in TreeItems verwandeln und hinzufügen
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBauteilPaare().get(k),anzahl));
			}
			//Rekursion für die Baugruppen der Baugruppe starten
			for(int l=0; l<bg.getStueckliste().getBaugruppenPaare().size();l++){
				//BaugruppenlPaare in TreeItems verwandeln und hinzufügen
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBaugruppenPaare().get(l),anzahl));
			}
			//Anzahl aktualisieren
			elementPaar.setAnzahl(multi);
			//Dem BaugruppenStack hinzufügen
			baugruppen.addLast(elementPaar);
		}
		
		//TreeItem zurückgeben
		return newTreeItem;
		
	}
	
	public Vector<ElementPaar> addiereBauteile (){
		Vector<ElementPaar> bauteilVector = new Vector<ElementPaar>();
		//Das erste Bauteil dem Vector hinzufügen
		bauteilVector.add(bauteile.get(0));
		//Den Vector ab index 1 komplett durchlaufen
		for(int i=1; i<bauteile.size();i++){
			//Jeweiliges ElementPaar mit dem bauteilVector vergleichen
			Boolean gefunden=false;
			for(int j=0; j<bauteilVector.size();j++){
				if(bauteile.get(i).getElement().getId()==bauteilVector.get(j).getElement().getId()){
					//Anzahl addieren
					int anzahl= bauteile.get(i).getAnzahl() + bauteilVector.get(j).getAnzahl();
					//Anzahl aktualisieren
					bauteilVector.get(j).setAnzahl(anzahl);
					gefunden=true;
				}
			}
			//Falls das Bauteil nicht gefunden wurde dem bauteilVector hinzufügen
			if(gefunden==false){
				bauteilVector.add(bauteile.get(i));
			}
		}
		//Vector zurückgeben
		return bauteilVector;
		
	}
	
	public Vector<ElementPaar> addiereBaugruppen (){
		Vector<ElementPaar> baugruppenVector = new Vector<ElementPaar>();
		//Die erste Baugruppe dem Vector hinzufügen
		baugruppenVector.add(baugruppen.get(0));
		//Den Vector ab index 1 komplett durchlaufen
		for(int i=1; i<baugruppen.size();i++){
			//Jeweiliges ElementPaar mit dem baugruppenVector vergleichen
			Boolean gefunden=false;
			for(int j=0; j<baugruppenVector.size();j++){
				if(baugruppen.get(i).getElement().getId()==baugruppenVector.get(j).getElement().getId()){
					//Anzahl addieren
					int anzahl= baugruppen.get(i).getAnzahl() + baugruppenVector.get(j).getAnzahl();
					//Anzahl aktualisieren
					baugruppenVector.get(j).setAnzahl(anzahl);
					gefunden=true;
				}
			}
			//Falls die Baugruppe nicht gefunden wurde dem baugruppenVector hinzufügen
			if(gefunden==false){
				baugruppenVector.add(baugruppen.get(i));
			}
		}
		//Vector zurückgeben
		return baugruppenVector;
		
	}
}