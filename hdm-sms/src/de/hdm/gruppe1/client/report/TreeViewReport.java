package de.hdm.gruppe1.client.report;

import java.util.Vector;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.ElementPaar;;

/**
 * Die Klasse TreeViewReport bietet dem Nutzer die Möglichkeit, eine Stückliste komplett anzuzeigen, inklusive der
 * zugehörigen Bauteile und Baugruppen. Diese Informationen werden in das sogenannte Tree-Widget von GWT
 * geladen. Da im Report dieser Tree nicht in Form eines Widgets, sondern als HTML angezeigt werden soll, erbt diese
 * Klasse von "Tree". Dadurch entsteht die Möglichkeit, die komplette Klasse aufzurufen und in einer Variable
 * zwischen zu speichern. Diese Variable kann dann mit der .toString-Methode in einen HTML Baum umgewandelt werden.
 * 
 * Außerdem bietet diese Klasse zwei Methoden, um alle Bauteile, bzw. Baugruppen im Baum mit der zugehörigen Anzahl
 * zu verrechnen. Sobald ein Bauteil mehr als zweimal im Baum auftaucht, rechnet die entsprechende Methode diese
 * zusammen. Auch ist es möglich, bei Eingabe von mehreren gewünschten Enderzeugnissen, den gesammelten Material-
 * bedarf anzuzeigen. Somit kann in der Praxis für die Produktion eine exakte Mengenangabe aller benötigten Bauteil,
 * bzw. Baugruppen erstellt werden.
 * 
 * Wird diese Klasse aus "Strukturstuecklisten" aufgerufen, ist die Anzahl immer 1, da nur eine Baugruppe angezeigt
 * werden soll.
 * Wird diese Klasse aus "Materialbedarf" aufgerufen, kann eine gewünschte Anzahl angegeben werden. Diese wird
 * benötigt, um alle identischen Bauteile, bzw. Baugruppen zusammengerechnet anzuzeigen.
 * 
 * @author Andreas Herrmann, Mario Theiler
 *
 */
public class TreeViewReport extends Tree {

	/**
	 * Vektoren für Bauteile und Baugruppen für die Addition derer.
	 */
	private Vector<ElementPaar> bauteile = new Vector<ElementPaar>();
	private Vector<ElementPaar> baugruppen = new Vector<ElementPaar>();
	
	/**
	 * Der Konstruktor bekommen ein Stücklisten-Objekt und eine Anzahl mitgeliefert und verarbeitet diese.
	 * 
	 * @param stueckliste
	 * @param anzahl
	 */
	public TreeViewReport (Stueckliste stueckliste, int anzahl){

		/**
		 * RootTreeItem erstellen und Anzahl und Namen der Stueckliste als Text hinzufügen.
		 */
		TreeItem rootTreeItem = new TreeItem();
		rootTreeItem.setText("Stückliste '"+stueckliste.getName()+"'");

		/**
		 * Treekursion für alle Bauteile der Stückliste starten.
		 */
		for(int i=0;i<stueckliste.getBauteilPaare().size();i++){

			/**
			 * BauteilPaare in TreeItems verwandeln und der RootTreeItem hinzufügen.
			 */
			rootTreeItem.addItem(this.treeRecursion(stueckliste.getBauteilPaare().get(i), anzahl));
		}

		/**
		 * Rekursion für alle Baugruppen der Stückliste durchführen.
		 */
		for(int j=0; j<stueckliste.getBaugruppenPaare().size();j++){

			/**
			 * BaugruppenPaare in TreeItems verwandeln und dem RootTreeItem hinzufügen.
			 */
			rootTreeItem.addItem(this.treeRecursion(stueckliste.getBaugruppenPaare().get(j), anzahl));
		}

		/**
		 * RootTreeItem dem Baum hinzufügen.
		 */
		this.addItem(rootTreeItem);
	}
	
	/**
	 * Mithilfe der rekursiven Methode "treeRecursion" wird ein Stücklisten-Objekt bis zu seinen Wurzeln durchlaufen
	 * und die jeweiligen Bauteile und Baugruppen werden der zugehörigen Ebene zugeordnet und dem Tree-Widget
	 * hinzugefügt.
	 * 
	 * @param elementPaar
	 * @param anzahl
	 * @return newTreeItem
	 */
	public TreeItem treeRecursion (ElementPaar elementPaar, int anzahl){
		TreeItem newTreeItem = new TreeItem();

		/**
		 * Anzahl mit der Anzahl im ElementPaar multiplizieren.
		 */
		int multi = anzahl*elementPaar.getAnzahl();

		/**
		 * Überprüfen ob das ElementPaar ein Bauteil oder eine Baugruppe enthält.
		 */
		if(elementPaar.getElement() instanceof Bauteil){

			/**
			 * Bauteil TreeItem erstellen.
			 */
			newTreeItem.setText(elementPaar.getAnzahl()+"*Bauteil '"+elementPaar.getElement().getName()+"'");

			/**
			 * Anzahl aktualisieren.
			 */
			elementPaar.setAnzahl(multi);

			/**
			 * Dem BauteilVector hinzufügen.
			 */
			bauteile.add(elementPaar);
		}

		/**
		 * Baugruppe.
		 */
		if(elementPaar.getElement() instanceof Baugruppe){

			/**
			 * Baugruppe TreeItem erstellen.
			 */
			newTreeItem.setText(elementPaar.getAnzahl()+"*Baugruppe '"+elementPaar.getElement().getName()+"'");
			Baugruppe bg = (Baugruppe) elementPaar.getElement();

			/**
			 * Rekursion für die Bauteile der Baugruppe starten.
			 */
			for(int k=0; k<bg.getStueckliste().getBauteilPaare().size(); k++){

				/**
				 * BauteilPaare in TreeItems verwandeln und hinzufügen.
				 */
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBauteilPaare().get(k),anzahl));
			}

			/**
			 * Rekursion für die Baugruppen der Baugruppe starten.
			 */
			for(int l=0; l<bg.getStueckliste().getBaugruppenPaare().size();l++){

				/**
				 * BaugruppenlPaare in TreeItems verwandeln und hinzufügen.
				 */
				newTreeItem.addItem(this.treeRecursion(bg.getStueckliste().getBaugruppenPaare().get(l),anzahl));
			}

			/**
			 * Anzahl aktualisieren.
			 */
			elementPaar.setAnzahl(multi);

			/**
			 * Dem Baugruppen-Vektor hinzufügen.
			 */
			baugruppen.add(elementPaar);
		}
		
		/**
		 * Das TreeItem zurückgeben.
		 */
		return newTreeItem;
		
	}
	
	/**
	 * Mithilfe der Methode addiereBauteile werden alle Bauteile aus dem Baum-Vektor durchlaufen und sobald ein
	 * Bauteil auftaucht, welches mit derselben Id bereits im Ergebnis-Vektor vorhanden ist, wird hier die Anzahl
	 * dieser beiden Bauteile miteinander verrechnet.
	 * 
	 * @return bauteilVector
	 */
	public Vector<ElementPaar> addiereBauteile (){
		
		/**
		 * Dieser bauteilVector dient als ErgebnisVector, der am Ende der Methode befüllt zurückgegeben wird.
		 */
		Vector<ElementPaar> bauteilVector = new Vector<ElementPaar>();

		/**
		 * Das erste Bauteil dem Vector hinzufügen.
		 */
		bauteilVector.add(bauteile.get(0));

		/**
		 * Den Vector ab index 1 komplett durchlaufen, da auf index 0 bereits das erste Bauteil platziert wurde.
		 */
		for(int i=1; i<bauteile.size();i++){

			/**
			 * Jeweiliges ElementPaar mit dem bauteilVector vergleichen.
			 */
			Boolean gefunden=false;
			for(int j=0; j<bauteilVector.size();j++){
				if(bauteile.get(i).getElement().getId()==bauteilVector.get(j).getElement().getId()){

					/**
					 * Die Anzahl addieren.
					 */
					int anzahl= bauteile.get(i).getAnzahl() + bauteilVector.get(j).getAnzahl();

					/**
					 * Die Anzahl aktualisieren.
					 */
					bauteilVector.get(j).setAnzahl(anzahl);
					gefunden=true;
				}
			}

			/**
			 * Falls das Bauteil nicht gefunden wurde, dem bauteilVector hinzufügen.
			 */
			if(gefunden==false){
				bauteilVector.add(bauteile.get(i));
			}
		}

		/**
		 * Den Ergebnis-Vector mit den verrechneten Bauteilen zurückgeben.
		 */
		return bauteilVector;
		
	}
	
	/**
	 * Mithilfe der Methode addiereBauteile werden alle Bauteile aus dem Baum-Vektor durchlaufen und sobald ein
	 * Bauteil auftaucht, welches mit derselben Id bereits im Ergebnis-Vektor vorhanden ist, wird hier die Anzahl
	 * dieser beiden Bauteile miteinander verrechnet.
	 * 
	 * @return baugruppenVector
	 */
	public Vector<ElementPaar> addiereBaugruppen (){
		
		/**
		 * Dieser baugruppenVector dient als ErgebnisVector, der am Ende der Methode befüllt zurückgegeben wird.
		 */
		Vector<ElementPaar> baugruppenVector = new Vector<ElementPaar>();

		/**
		 * Die erste Baugruppe dem Vector hinzufügen.
		 */
		baugruppenVector.add(baugruppen.get(0));

		/**
		 * Den Vector ab index 1 komplett durchlaufen, da auf index 0 bereits die erste Baugruppe platziert wurde.
		 */
		for(int i=1; i<baugruppen.size();i++){

			/**
			 * Jeweiliges ElementPaar mit dem baugruppenVector vergleichen.
			 */
			Boolean gefunden=false;
			for(int j=0; j<baugruppenVector.size();j++){
				if(baugruppen.get(i).getElement().getId()==baugruppenVector.get(j).getElement().getId()){

					/**
					 * Die Anzahl addieren.
					 */
					int anzahl= baugruppen.get(i).getAnzahl() + baugruppenVector.get(j).getAnzahl();

					/**
					 * Die Anzahl aktualisieren.
					 */
					baugruppenVector.get(j).setAnzahl(anzahl);
					gefunden=true;
				}
			}

			/**
			 * Falls die Baugruppe nicht gefunden wurde, dem baugruppenVector hinzufügen.
			 */
			if(gefunden==false){
				baugruppenVector.add(baugruppen.get(i));
			}
		}

		/**
		 * Den Ergebnis-Vector mit den verrechneten Baugruppen zurückgeben.
		 */
		return baugruppenVector;
		
	}
}
