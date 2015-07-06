import java.util.ArrayDeque;
import java.util.Vector;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.ElementPaar;;

public class TreeView extends Tree {
	//Stacks für Bauteile und Baugruppen für die Addition derer
	private ArrayDeque<ElementPaar> bauteile = new ArrayDeque<ElementPaar>();
	private ArrayDeque<ElementPaar> baugruppen = new ArrayDeque<ElementPaar>();
	
	public TreeView (Stueckliste stueckliste, int anzahl){
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
			bauteile.addLast(elementPaar);
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
		//Den Stack solange durchlaufen bis er leer ist
		while(!bauteile.isEmpty()){
			//Prüfen, ob das erste Bauteil schon im Vector ist
			if(bauteilVector.contains(bauteile.getFirst())){
				//Index finden
				int index = bauteilVector.indexOf(bauteile.getFirst());
				//Anzahl aktualisieren
				int anzahl = bauteilVector.get(index).getAnzahl() + bauteile.removeFirst().getAnzahl();
				bauteilVector.get(index).setAnzahl(anzahl);
			}
			//Wenn nicht
			else{
				//Dem Vector hinzufügen
				bauteilVector.addElement(bauteile.removeFirst());
			}
		}
		//Vector zurückgeben
		return bauteilVector;
		
	}
	
	public Vector<ElementPaar> addiereBaugruppen (){
		Vector<ElementPaar> baugruppenVector = new Vector<ElementPaar>();
		//Den Stack solange durchlaufen bis er leer ist
		while(!baugruppen.isEmpty()){
			//Prüfen, ob das erste Bauteil schon im Vector ist
			if(baugruppenVector.contains(baugruppen.getFirst())){
				//Index finden
				int index = baugruppenVector.indexOf(baugruppen.getFirst());
				//Anzahl aktualisieren
				int anzahl = baugruppenVector.get(index).getAnzahl() + baugruppen.removeFirst().getAnzahl();
				baugruppenVector.get(index).setAnzahl(anzahl);
			}
			//Wenn nicht
			else{
				//Dem Vector hinzufügen
				baugruppenVector.addElement(bauteile.getFirst());
			}
		}
		//Vector zurückgeben
		return baugruppenVector;
		
	}
}