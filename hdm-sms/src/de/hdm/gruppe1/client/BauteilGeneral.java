package de.hdm.gruppe1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Die Klasse BauteilGeneral liefert eine �bersicht mit allen vorhandenen Bauteilen im System
 * und bietet M�glichkeiten, diese anzulegen, zu editieren oder zu l�schen.
 */
public class BauteilGeneral implements IsWidget {

	@Override
	public Widget asWidget() {
		return null;
	}
	
	//Vertikales Panel, um alle relevanten Elemente f�r Bauteile anzuordnen.
	private final VerticalPanel BauteilOverviewPanel = new VerticalPanel ();
	
	//Elemente f�r Bauteile initialisieren
	private final Label HeadlineLabel = new Label ("Bauteil�bersicht");
	private final Label SublineLabel = new Label ("In dieser �bersicht sehen Sie alle im System vorhandenen Bauteile. Um diese zu editieren oder l�schen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Bauteil anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	private final Button NewBauteilButton = new Button ("Neues Bauteil");
	private final FlexTable Overview = new FlexTable ();

	public BauteilGeneral() {

		BauteilOverviewPanel.add(HeadlineLabel);
		BauteilOverviewPanel.add(SublineLabel);
		BauteilOverviewPanel.add(NewBauteilButton);
		BauteilOverviewPanel.add(Overview);
		RootPanel.get("content_wrap").add(BauteilOverviewPanel);
		
		NewBauteilButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				RootPanel.get("content_wrap").clear();
				Window.alert("Platzhalter f�r RPC-Funktion: Neues Bauteil anlegen");
			    }

		});
		
		Overview.setText(1, 0, "Nummer");
		Overview.setText(1, 1, "Bezeichnung");
		Overview.setText(1, 2, "Beschreibung");
		Overview.setText(1, 3, "Materialbezeichnung");
		Overview.setText(1, 4, "Letzter �nderer");
		Overview.setText(1, 5, "Datum letzte �nderung");
		Overview.setWidget(1, 6, new Button("Edit"));
		Overview.setWidget(1, 7, new Button("Delete"));

	}

}
