package de.hdm.gruppe1.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe1.client.EnderzeugnisGeneralView.GetAllEnderzeugnisseCallback;
//import de.hdm.gruppe1.client.CreateEnderzeugnis.CreateEnderzeugnisCallback;
//import de.hdm.gruppe1.client.EnderzeugnisGeneralView.DeleteEnderzeugnisCallback;

import de.hdm.gruppe1.shared.FieldVerifier;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Bauteil;
import de.hdm.gruppe1.shared.bo.Baugruppe;
import de.hdm.gruppe1.shared.bo.Stueckliste;
import de.hdm.gruppe1.shared.bo.Enderzeugnis;

/**
 * Mithilfe der Klasse CreateEnderzeugnis wird dem User der Applikation ermöglicht,
 * ein aus Baugruppen erzeugtes Enderzeugnis-Objekt in die Datenbank anzulegen.
 * 
 */

public class CreateEnderzeugnis extends VerticalPanel {
	
	/**
	 * GUI-Elemente für CreateEnderzeugnis zu initialisieren
	 */
	
	//Elemente für Create Enderzeugnis  initialisieren
		private final Label HeadlineLabel = new Label ("Enderzeugnis anlegen");
		private final Label SublineLabel = new Label ("Um ein Enderezugnis anzulegen, geben sie bitte einen Namen an und wählen sie die dazugehöhrige Baugruppe aus, die sie als Enderzeugnis festlegen möchten und bestätigen sie Ihre Auswahl mit dem <anlegen>-Button.");

		private final Label NameFieldLabel = new Label("Name");
		private final TextBox NameField = new TextBox();
		
		//Baugruppen für Enderzeugnisse zu initialisieren
		private final Label OverviewLabel = new Label ("Baugruppennübersicht");
		
		
		//Button zum Enderzeugnis erstellen
		private final Button CreateEnderzeugnisButton = new Button ("anlegen");
		
		//Enderzeugnis, das angelegt werden soll
		Enderzeugnis createEnderzeugnis = null;
		
	
		//Panels, um die hinzuf�gen-Buttons neben den Dropdowns zu platzieren
		HorizontalPanel bgPanel = new HorizontalPanel();
		
		
		//TODO implementieren
		//Vektor wird mit allen Baugruppen aus der DB bef�llt
		Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
		
		private final FlexTable table = new FlexTable();
		
		//Tabellen, um in der GUI alle Baugruppen anzuzeigen, bevor das Enderzeugnis gespeichert wird
		FlexTable baugruppeCollection = new FlexTable();
		
		// Remote Service via ClientsideSettings
		SmsAsync stuecklistenVerwaltung= ClientsideSettings.getSmsVerwaltung();
		


	public CreateEnderzeugnis(){
		
		//Damit die edit und delete Buttons horizontal angeordnet werden, müssen diese dem ButtonPanel zugeordnet werden
				
				HeadlineLabel.setStyleName("headline");
				table.setStyleName("tableBody");
	
		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());
		
		//Die erste Reihe der Tabelle wird mit Überschriften vordefiniert
				table.setText(0, 0, "ID");
				table.setText(0, 1, "Name");
				table.setText(0, 2, "Erstellungsdatum");
				table.setText(0, 3, "Letzter Änderer");
				table.setText(0, 4, "Letztes Änderungsdatum");
				table.setText(0, 5, "Auswahl");
				
				
				//Das FlexTable Widget unterstützt keine Headlines. Daher wird die erste Reihe über folgenden Umweg formatiert
				table.getCellFormatter().addStyleName(0, 0, "tableHead");
				table.getCellFormatter().addStyleName(0, 1, "tableHead");
				table.getCellFormatter().addStyleName(0, 2, "tableHead");
				table.getCellFormatter().addStyleName(0, 3, "tableHead");
				table.getCellFormatter().addStyleName(0, 4, "tableHead");
				table.getCellFormatter().addStyleName(0, 5, "tableHead");
					    
				this.add(HeadlineLabel);
				this.add(SublineLabel);
				this.add(NameFieldLabel);
				this.add(NameField);
				this.add(OverviewLabel);
				this.add(table);
				this.add(CreateEnderzeugnisButton);
					    
				RootPanel.get("content_wrap").add(this);




					
				}
				
			
	/*
	 * Click Handlers.
	 */
	
	 class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Baugruppe konnten nicht geladen werden");
			}

			@Override
			public void onSuccess(Vector<Baugruppe> result) {

				allBaugruppen = result;
				
				Window.alert("Inhalt: "+allBaugruppen.toString());
				
				if (allBaugruppen.isEmpty() == true){
					
//					table.getFlexCellFormatter().setColSpan(1, 0, 6);
//
//					table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));
					Window.alert("Inhalt: "+allBaugruppen.toString());
					
				} 
		
			}}}



