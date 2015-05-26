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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.gruppe1.shared.SmsAsync;
import de.hdm.gruppe1.shared.bo.Baugruppe;

/*
 * Die Klasse BaugruppeGeneralView liefert eine Übersicht mit allen vorhandenen Baugruppen im System
 * und bietet Möglichkeiten, diese anzulegen, zu editieren oder zu löschen.
 */

public class BaugruppeGeneralView extends VerticalPanel {
	
	/**
	 * Überschrift, um dem User eine Orientierung zu geben, in welchem Bereich
	 * der Applikation er sich befindet.
	 */

	private final Label HeadlineLabel = new Label ("Baugruppeübersicht");
	// private final Label SublineLabel = new Label ("In dieser Übersicht sehen Sie alle im System vorhandenen Baugruppen. Um diese zu editieren oder löschen, klicken Sie in der Tabelle auf den entsprechenden Button. Um ein neues Baugruppe anzulegen, klicken Sie auf den <Neues Bauteil>-Button.");
	
	/**
	 * Buttons sollen nebeneinander angezeigt werden, nicht vertikal. Daher wird
	 * ein "vertikales Zwischen-Panel" benötigt.
	 */
	
	private HorizontalPanel editButtonPanel = new HorizontalPanel();
	private HorizontalPanel deleteButtonPanel = new HorizontalPanel();
	
	/**
	 * Den Buttons wird jeweils ein erklärender Text hinzugefügt.
	 */
	
	private final Label editLabel = new Label("Wählen Sie in der Übersicht eine Baugruppe aus, um es mithilfe dieses Buttons zu editieren: ");
	private final Label deleteLabel = new Label("Wählen Sie in der Übersicht mindestens eine Baugruppe aus, um es mithilfe dieses Buttons zu löschen: ");
	
	
	/**
	 * Die RadioButtons und CheckBoxen erhalten jeweils einen globalen edit-
	 * bzw. delete-Button. Dies entspricht dem neuesten Stand der
	 * Web-Programmierung.
	 */
	
	private final Button editBtn = new Button("");
	private final Button deleteBtn = new Button("");
	
	/**
	 * Tabelle, in der jegliche Baugruppe inkl. edit- & delete-Buttons angezeigt
	 * werden.
	 */
	
	private final FlexTable table = new FlexTable();
	
	/**
	 * Baugruppe, welches editiert werden soll.
	 */
	Baugruppe editBaugruppe = null;

	/** 
	 * Vektor, der mit allen Baugruppen aus der DB befüllt wird.
	 */
	
	Vector<Baugruppe> allBaugruppen = new Vector<Baugruppe>();
	
	Vector<Baugruppe> deleteBaugruppe = new Vector<Baugruppe>();
	
	/**
	 * Remote Service via ClientsideSettings Wird an dieser Stelle einmalig in
	 * der Klasse aufgerufen. Im Anschluss kann jederzeit darauf zugegriffen
	 * werden.
	 */
	SmsAsync stuecklistenVerwaltung = ClientsideSettings.getSmsVerwaltung();
	
	public BaugruppeGeneralView() {
		
		/**
		 * Damit die edit und delete Buttons horizontal angeordnet werden,
		 * müssen diese dem ButtonPanel zugeordnet werden.
		 */
	
		editButtonPanel.add(editLabel);
		editButtonPanel.add(editBtn);
		
		deleteBtn.addClickHandler(new deleteClickHandler());
		deleteButtonPanel.add(deleteLabel);
		deleteButtonPanel.add(deleteBtn);
		
		editBtn.setStyleName("editButton");
		deleteBtn.setStyleName("deleteButton");
		HeadlineLabel.setStyleName("headline");
		table.setStyleName("BaugruppeTable");	
		
		/**
		 * RPC-Methode ausführen, die alle Baugruppen-Objekte aus der Datenbank in
		 * einem Vektor zurückliefert. Dadurch wird der Klassen-Vektor
		 * "allBaugruppen" befüllt.
		 */
		

		stuecklistenVerwaltung.getAllBaugruppen(new GetAllBaugruppenCallback());

		/**
		 * Die erste Reihe der Tabelle wird mit Überschriften vordefiniert.
		 */
		
	    table.setText(0, 0, "ID");
	    table.setText(0, 1, "Name");
	    table.setText(0, 2, "Letzter Änderer");
	    table.setText(0, 3, "Letztes Änderungsdatum");
	    table.setText(0, 4, "Editieren");
	    table.setText(0, 5, "Löschen");
	    
	    /**
		 * Das FlexTable Widget unterstützt keine Headlines. Daher wird die
		 * erste Reihe über folgenden Umweg formatiert.
		 */
	    
	    table.getCellFormatter().addStyleName(0, 0, "tableHead");
	    table.getCellFormatter().addStyleName(0, 1, "tableHead");
	    table.getCellFormatter().addStyleName(0, 2, "tableHead");
	    table.getCellFormatter().addStyleName(0, 3, "tableHead");
	    table.getCellFormatter().addStyleName(0, 4, "tableHead");
	    table.getCellFormatter().addStyleName(0, 5, "tableHead");
	    
	    /**
		 * Nachdem alle Elemente geladen sind, wird alles dem VerticalPanel
		 * zugeordnet, da diese Klasse von VerticalPanel erbt.
		 */
	    
		this.add(HeadlineLabel);
		this.add(editButtonPanel);
		this.add(deleteButtonPanel);
		this.add(table);
		
		/**
		 * Abschließend wird alles dem RootPanel zugeordnet
		 */
	    
		RootPanel.get("content_wrap").add(this);
		
		
	}
	
	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die einen Vektor von allen in
	 * der DB vorhandenen Baugruppen liefert. Die Klasse ist eine nested-class
	 * und erlaubt daher, auf die Attribute der übergeordneten Klasse
	 * zuzugreifen.
	 */
	
	class GetAllBaugruppenCallback implements AsyncCallback<Vector<Baugruppe>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Baugruppen konnten nicht geladen werden");
		}
		
		@Override
		public void onSuccess(Vector<Baugruppe> alleBaugruppen) {

			/**
			 * Der Baugruppen-Vektor allBaugruppe wird mit dem Ergebnis dieses RPC´s
			 * befüllt.
			 */
			allBaugruppen = alleBaugruppen;

			if (allBaugruppen.isEmpty() == true){
				
				table.getFlexCellFormatter().setColSpan(1, 0, 5);

				table.setWidget(1, 0, new Label("Es sind leider keine Daten in der Datenbank vorhanden."));
				
			}
			
			else {
			/**
			 * Die flexTable table wird mithilfe dieser for-Schleife Reihe um
			 * Reihe für jede Baugruppe befüllt.
			 */
			for (int row = 1; row <= allBaugruppen.size(); row++) {

				/**
				 * Da die flexTable in Reihen-Index 0 bereits mit den
				 * Tabellen-Überschriften belegt ist (Begründung siehe weiter
				 * oben im Code), wird eine "Hilfs-Variable" benötigt, die den
				 * Tabellen-Index für den Vektor-Index simuliert.
				 */
				final int i = row - 1;

				CheckBox checkBox = new CheckBox("");
				RadioButton radioButton = new RadioButton("editRadioGroup", "");

				/**
				 * Pro Vektor-Index wird eine Reihe in die Tabelle geschrieben.
				 */
				table.setText(row, 0, "" + allBaugruppen.get(i).getId());
				table.setText(row, 1, allBaugruppen.get(i).getName());
				table.setText(row, 2, "Katja");
				table.setText(row, 3, "02.05.2015, 18 Uhr");

				/**
				 * An dieser Stelle wird pro Schleifendurchlauf ein RadioButton
				 * Widget hinzugefügt. Mithilfe der Eigenschaft von "RadioGroup"
				 * kann jeweils nur ein RadioButton, nach vollständigem Befüllen
				 * der Tabelle, ausgewählt werden.
				 */
				table.setWidget(row, 4, radioButton);

				/**
				 * Dieser RadioButton wird pro Reihe mit einem
				 * ValueChangeHandler erweitert. Dieser erkennt, welcher
				 * RadioButton ausgewählt ist und befüllt das Klassen-Objekt
				 * editBaugruppe von "Baugruppe" mit dem Objekt der entsprechenden
				 * Tabellen-Reihe.
				 */
				radioButton
						.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(
									ValueChangeEvent<Boolean> e) {
								if (e.getValue() == true) {
									editBaugruppe = allBaugruppen.get(i);
								}
							}
						});
				
				/**
				 * An dieser Stelle wird pro Schleifendurchlauf ein CheckBox
				 * Widget hinzugefügt.
				 */
				table.setWidget(row, 5, checkBox);
				
				checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<Boolean> e) {
						if (e.getValue() == true) {
							Baugruppe deleteBaugruppe = allBaugruppen.get(i);
							deleteBaugruppe.add(deleteBaugruppe);
							Window.alert("Inhalt Vektor: "+deleteBaugruppe.toString());
						} else if (e.getValue() == false) {
							Baugruppe removeBaugruppe = allBaugruppen.get(i);
							deleteBaugruppe.remove(removeBaugruppe);
							Window.alert("Gelöscht: "+deleteBaugruppe.toString());
						}
					}
				});
				
				/**
				 * Die Tabelle erhält ein css-Element für den Body, welches sich
				 * vom css-Element für die Überschriften unterscheidet.
				 */
				table.setStyleName("tableBody");

			}
		}

			/**
			 * ClickHandler für den Aufruf der Klasse editBaugruppe. Als Attribut
			 * wird das Baugruppen-Objekt aus der entsprechenden Tabellen-Reihe
			 * mitgeschickt.
			 */
			
			editBtn.addClickHandler(new ClickHandler() {
				
			
				public void onClick(ClickEvent event) {

					if (editBaugruppe == null) {
						Window.alert("Bitte wählen Sie eine Baugruppe zum editieren aus.");
					} else {
						RootPanel.get("content_wrap").clear();
						RootPanel.get("content_wrap").add(
								new EditBaugruppe(editBaugruppe));
					}

				}

			});

		}
	}


	
	/**
	 * Hiermit wird die RPC-Methode aufgerufen, die ein Baugruppe-Objekt löscht
	 */

	private class deleteClickHandler implements ClickHandler  {
		@Override
		public void onClick(ClickEvent event) {
		  if (deleteBaugruppe.isEmpty() == true){
				Window.alert("Es wurde keine Baugruppe zum Löschen ausgewählt.");
			}
			
			else {
			for (int i=0;i<=deleteBaugruppe.size();i++) {
			Baugruppe bg = new Baugruppe();
			bg = deleteBaugruppe.get(i);
				/**
				 * Die konkrete RPC-Methode für den create-Befehl wird
				 * aufgerufen. Hierbei werden die gewünschten Werte
				 * mitgeschickt.
				 */
				stuecklistenVerwaltung.delete(bg,new DeleteBaugruppeCallback());
			}
			}
		}
	}

	  class DeleteBaugruppeCallback implements AsyncCallback<Void> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Das Loeschen der Baugruppe ist fehlgeschlagen!");
			}

			@Override
			public void onSuccess(Void result) {

				Window.alert("Die Baugruppe wurde erfolgreich geloescht.");
			}
		}
	}

		