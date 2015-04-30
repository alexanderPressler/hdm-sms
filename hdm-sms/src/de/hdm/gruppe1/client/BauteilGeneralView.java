package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Die Klasse BauteilGeneral liefert eine Übersicht mit allen vorhandenen Bauteilen im System
 * und bietet Möglichkeiten, diese anzulegen, zu editieren oder zu löschen.
 */
public class BauteilGeneralView extends VerticalPanel {

	//Elemente für Bauteile initialisieren
	private final Label HeadlineLabel = new Label ("Bauteilübersicht");
	private final Label SublineLabel = new Label ("In dieser Übersicht sehen Sie alle im System vorhandenen Bauteile. Um diese zu editieren oder löschen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Bauteil anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	private final Button NewBauteilButton = new Button ("Neues Bauteil");
	private final Label OverviewTableLabel = new Label ("Diese Tabelle enthält eine Übersicht über alle Bauteile im System");
	private final FlexTable Overview = new FlexTable ();

	public BauteilGeneralView() {

		this.add(HeadlineLabel);
		this.add(SublineLabel);
		this.add(NewBauteilButton);
		this.add(OverviewTableLabel);
		this.add(Overview);
		
		NewBauteilButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new CreateBauteil());
//				Window.alert("Platzhalter für RPC-Funktion: Neues Bauteil anlegen");
			    }

		});
		
		//Applikationsschicht liefert <Bauteil>-Vector.
		//Diesen mithilfe for-Schleife durchlaufen und angemessen darstellen.
		//Hier sind aktuell lediglich die Tabellenüberschriften definiert.
		
		Overview.setText(1, 0, "Nummer");
		Overview.setText(1, 1, "Bezeichnung");
		Overview.setText(1, 2, "Beschreibung");
		Overview.setText(1, 3, "Materialbezeichnung");
		Overview.setText(1, 4, "Letzter Änderer");
		Overview.setText(1, 5, "Datum letzte Änderung");
		Overview.setWidget(1, 6, new Button("Edit"));
		Overview.setWidget(1, 7, new Button("Delete"));

		RootPanel.get("content_wrap").add(this);
		
	}

}
